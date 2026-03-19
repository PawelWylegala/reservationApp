package pl.wylegala.room_reservation2.domain.reservation;

import jakarta.persistence.*;
import pl.wylegala.room_reservation2.domain.room.Room;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Embedded
    private TimeSlot timeSlot;

    @Column(nullable = false)
    private String reservedBy;

    protected Reservation() {}

    public Reservation(Room room, TimeSlot timeSlot, String reservedBy) {
        this.room = room;
        this.timeSlot = timeSlot;
        this.reservedBy = reservedBy;
        this.status = ReservationStatus.ACTIVE;
    }

    public void cancel(){
        if (status == ReservationStatus.CANCELLED){
            throw new IllegalStateException("Rezerwacja jest już anulowana");
        }
        if (LocalDateTime.now().isAfter(timeSlot.getStartTime().minusHours(24))){
            throw new IllegalStateException("Nie można anulowaćrezerwacji na mniej niż 24h przed terminem");
        }

        this.status = ReservationStatus.CANCELLED;

    }



    public UUID getId() { return id; }
    public Room getRoom() { return room; }
    public TimeSlot getTimeSlot() { return timeSlot; }
    public String getReservedBy() { return reservedBy; }
    public ReservationStatus getStatus() {return status;}
}