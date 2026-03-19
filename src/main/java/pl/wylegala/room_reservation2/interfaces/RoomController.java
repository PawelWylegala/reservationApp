package pl.wylegala.room_reservation2.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wylegala.room_reservation2.application.ReservationService;
import pl.wylegala.room_reservation2.application.RoomMapper;
import pl.wylegala.room_reservation2.domain.reservation.TimeSlot;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final ReservationService reservationService;
    private final RoomMapper roomMapper;

    public RoomController(ReservationService reservationService, RoomMapper roomMapper) {
        this.reservationService = reservationService;
        this.roomMapper = roomMapper;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody CreateRoomRequest request) {
        return ResponseEntity.ok(
                roomMapper.toRoomResponse(
                        reservationService.createRoom(request.name(), request.capacity())
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(
                reservationService.getAllRooms()
                        .stream()
                        .map(roomMapper::toRoomResponse)
                        .toList()
        );
    }

    @PostMapping("/{roomId}/reservations")
    public ResponseEntity<ReservationResponse> makeReservation(
            @PathVariable UUID roomId,
            @RequestBody CreateReservationRequest request) {
        TimeSlot timeSlot = new TimeSlot(request.startTime(), request.endTime());
        return ResponseEntity.ok(
                roomMapper.toReservationResponse(
                        reservationService.makeReservation(roomId, timeSlot, request.reservedBy())
                )
        );
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(
                reservationService.getAllReservations()
                        .stream()
                        .map(roomMapper::toReservationResponse)
                        .toList()
        );
    }

    record CreateRoomRequest(String name, int capacity) {}

    record CreateReservationRequest(
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime startTime,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime endTime,
            String reservedBy
    ) {}

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable UUID reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}