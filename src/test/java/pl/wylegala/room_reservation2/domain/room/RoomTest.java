package pl.wylegala.room_reservation2.domain.room;

import org.junit.jupiter.api.Test;
import pl.wylegala.room_reservation2.domain.reservation.Reservation;
import pl.wylegala.room_reservation2.domain.reservation.TimeSlot;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomTest {


    @Test
    void powinienZwrocicTrueGdyBrakRezerwacji(){

        Room room = new Room("Sala A", 10);

        TimeSlot termin = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 10, 0),
                LocalDateTime.of(2026, 3, 20, 12, 0)
        );


        boolean wynik = room.isAvailableFor(termin);

        assertThat(wynik).isTrue();

    }

    @Test
    void powinienZwrocicFalseGdyTerminZajety() {
        // ARRANGE
        Room room = new Room("Sala A", 10);

        TimeSlot zajętyTermin = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 10, 0),
                LocalDateTime.of(2026, 3, 20, 12, 0)
        );
        room.addReservation(new Reservation(room, zajętyTermin, "Jan Kowalski"));

        TimeSlot nakładającySię = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 11, 0),
                LocalDateTime.of(2026, 3, 20, 13, 0)
        );

        // ACT
        boolean wynik = room.isAvailableFor(nakładającySię);

        // ASSERT
        assertThat(wynik).isFalse();
    }

    @Test
    void powinienZwrocicTrueGdyTerminyStykajaSieAleNieNakladaja(){
        Room room = new Room("Sala A", 10);

        TimeSlot zajety = new TimeSlot(
                LocalDateTime.of(2026,03, 20, 12, 00),
                LocalDateTime.of(2026, 03,20,14,00));

        room.addReservation(new Reservation(room,zajety,"Jan Kowalski"));


        TimeSlot stykajacy = new TimeSlot(
                LocalDateTime.of(2026,03, 20, 14, 00),
                LocalDateTime.of(2026, 03,20,16,00));


        boolean wynik = room.isAvailableFor(stykajacy);

        assertThat(wynik).isTrue();
    }



}
