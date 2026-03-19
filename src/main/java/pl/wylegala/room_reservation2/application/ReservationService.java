package pl.wylegala.room_reservation2.application;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wylegala.room_reservation2.domain.reservation.Reservation;
import pl.wylegala.room_reservation2.domain.reservation.ReservationRepository;
import pl.wylegala.room_reservation2.domain.reservation.ReservationStatus;
import pl.wylegala.room_reservation2.domain.reservation.TimeSlot;
import pl.wylegala.room_reservation2.domain.room.Room;
import pl.wylegala.room_reservation2.domain.room.RoomRepository;


import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(RoomRepository roomRepository,
                              ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Room createRoom(String name, int capacity) {
        if (roomRepository.existsByName(name)) {
            throw new IllegalArgumentException("Pokój o nazwie '" + name + "' już istnieje");
        }
        return roomRepository.save(new Room(name, capacity));
    }

    @Transactional
    public Reservation makeReservation(UUID roomId, TimeSlot timeSlot, String reservedBy) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pokoju: " + roomId));

        if (!room.isAvailableFor(timeSlot)) {
            throw new IllegalStateException("Pokój zajęty w podanym terminie");
        }

        return reservationRepository.save(new Reservation(room, timeSlot, reservedBy));
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Reservation> getAllReservations() {

        return reservationRepository.findByStatus(ReservationStatus.ACTIVE);
    }


    @Transactional
    public void cancelReservation(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono rezerwacji: " + reservationId));

        reservation.cancel();
        reservationRepository.save(reservation);
    }
}