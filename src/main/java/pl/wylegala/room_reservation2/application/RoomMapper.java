package pl.wylegala.room_reservation2.application;

import org.springframework.stereotype.Component;
import pl.wylegala.room_reservation2.domain.reservation.Reservation;
import pl.wylegala.room_reservation2.domain.room.Room;
import pl.wylegala.room_reservation2.interfaces.ReservationResponse;
import pl.wylegala.room_reservation2.interfaces.RoomResponse;

@Component
public class RoomMapper {

    public RoomResponse toRoomResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getCapacity()
        );
    }

    public ReservationResponse toReservationResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRoom().getId(),
                reservation.getRoom().getName(),
                reservation.getTimeSlot().getStartTime(),
                reservation.getTimeSlot().getEndTime(),
                reservation.getReservedBy(),
                reservation.getStatus()  // ← dodaj to
        );
    }
}
