package pl.wylegala.room_reservation2.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wylegala.room_reservation2.domain.reservation.Reservation;
import pl.wylegala.room_reservation2.domain.reservation.ReservationRepository;
import pl.wylegala.room_reservation2.domain.reservation.ReservationStatus;
import pl.wylegala.room_reservation2.domain.reservation.TimeSlot;
import pl.wylegala.room_reservation2.domain.room.Room;
import pl.wylegala.room_reservation2.domain.room.RoomRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void powinienUtworzycRezerwacjeGdyPokojWolny() {
        // ARRANGE
        UUID roomId = UUID.randomUUID();
        Room room = new Room("Sala A", 10);
        TimeSlot timeSlot = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 10, 0),
                LocalDateTime.of(2026, 3, 20, 12, 0)
        );

        // "oszukujemy" — mock zwraca nasz pokój gdy ktoś go szuka po ID
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        // "oszukujemy" — mock udaje że zapisał rezerwację
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        Reservation wynik = reservationService.makeReservation(roomId, timeSlot, "Jan Kowalski");

        // ASSERT
        assertThat(wynik.getReservedBy()).isEqualTo("Jan Kowalski");
        assertThat(wynik.getTimeSlot()).isEqualTo(timeSlot);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void powinienRzucicWyjatekGdyPokojNieIstnieje() {
        // ARRANGE
        UUID nieistniejaceId = UUID.randomUUID();
        when(roomRepository.findById(nieistniejaceId)).thenReturn(Optional.empty());

        TimeSlot timeSlot = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 10, 0),
                LocalDateTime.of(2026, 3, 20, 12, 0)
        );

        // ACT & ASSERT
        assertThatThrownBy(() ->
                reservationService.makeReservation(nieistniejaceId, timeSlot, "Jan Kowalski")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nie znaleziono pokoju");
    }

    @Test
    void powinienRzucicWyjatekGdyPokojZajety() {
        // ARRANGE
        UUID roomId = UUID.randomUUID();
        Room room = new Room("Sala A", 10);

        TimeSlot zajętyTermin = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 10, 0),
                LocalDateTime.of(2026, 3, 20, 12, 0)
        );
        room.addReservation(new Reservation(room, zajętyTermin, "Anna Nowak"));

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        TimeSlot nakładającySię = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 11, 0),
                LocalDateTime.of(2026, 3, 20, 13, 0)
        );

        // ACT & ASSERT
        assertThatThrownBy(() ->
                reservationService.makeReservation(roomId, nakładającySię, "Jan Kowalski")
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("zajęty");
    }

    @Test
    void powinienPokazacWszytskieDostepnePokoje() {
        // ARRANGE

        Room room = new Room("Sala A", 10);
        Room room2 = new Room("Sala B", 10);

        when(roomRepository.findAll()).thenReturn(List.of(room2, room));
        //ACT

        List<Room> wynik = reservationService.getAllRooms();

        //ASSERT

        assertThat(wynik).hasSize(2);
        assertThat(wynik).contains(room,room2);

    }

    @Test
    void powinienPokazacWszytskieRezerwacje(){

        // ARRANGE

        Room room = new Room("sala A",10);

         TimeSlot termin1 = new TimeSlot(LocalDateTime.of(2026,03,20,10,00),
                 LocalDateTime.of(2026,03,20,12,00));

        TimeSlot termin2 = new TimeSlot(LocalDateTime.of(2026,03,20,12,00),
                LocalDateTime.of(2026,03,20,14,00));

        when(reservationRepository.findByStatus(ReservationStatus.ACTIVE)).thenReturn(List.of(
                new Reservation(room,termin1,"Jan Kowalski"),
                new Reservation(room,termin2,"Paweł Kowalski")));

        // ACT
         List<Reservation> wynik = reservationService.getAllReservations();

        //ASSERT
        assertThat(wynik).hasSize(2);


    }

    @Test
    void powinienUtworzycPokojGdyNazwaJestWolna() {

        // ARRANGE
        String name = "Sala balowa";
        int capacity = 20;
        when(roomRepository.existsByName(name)).thenReturn(false);

        when(roomRepository.save(any(Room.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        Room wynik = reservationService.createRoom(name,capacity);

        //ASSERT
        assertThat(wynik.getName()).contains(name);

    }

    @Test
    void powinienRzucicWyjatekGdyPokojOTakiejNazwieJuzIstnieje() {

        //ARRANGE
        String name = "Sala Balowa";

        when(roomRepository.existsByName(name)).thenReturn(true);

       //ACT && ASSERT
        assertThatThrownBy(() -> reservationService.createRoom(name,30))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pokój o nazwie '" + name + "' już istnieje");

    }

    @Test
    void powinienAnulowacIstniejacaRezerwacje(){
        // ARRANGE
        UUID reservationId = UUID.randomUUID();
        Room room = new Room("Sala A",30);

        TimeSlot termin = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 10, 0),
                LocalDateTime.of(2026, 3, 20, 12, 0)
        );
       Reservation reservation = new Reservation(room,termin,"Jan Kowalski");

       when(reservationRepository.findById(reservationId)).thenReturn((Optional.of(reservation)));

        // ACT
        reservationService.cancelReservation(reservationId);

        //ASSERT
        verify(reservationRepository).save(reservation);


    }

    @Test
    void powinienRucicWyjatkiemPrzyAnulowaniuMniejNiz24h(){
        UUID reservationId = UUID.randomUUID();
        Room room = new Room("Sala A",30);

        TimeSlot termin = new TimeSlot(
                LocalDateTime.of(2026, 3, 19, 10, 0),
                LocalDateTime.of(2026, 3, 19, 12, 0)
        );
        Reservation reservation = new Reservation(room,termin,"Jan Kowalski");

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // ACT

        //ASSERT
        assertThatThrownBy(() -> reservationService.cancelReservation(reservationId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Nie można anulowaćrezerwacji na mniej niż 24h przed terminem");

    }
}