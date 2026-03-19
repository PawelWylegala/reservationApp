package pl.wylegala.room_reservation2.interfaces;

import pl.wylegala.room_reservation2.domain.reservation.ReservationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID roomId,
        String roomName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String reservedBy,
        ReservationStatus status
) {}
