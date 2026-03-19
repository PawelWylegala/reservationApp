package pl.wylegala.room_reservation2.domain.room;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.wylegala.room_reservation2.domain.reservation.Reservation;
import pl.wylegala.room_reservation2.domain.reservation.TimeSlot;
import pl.wylegala.room_reservation2.domain.reservation.Reservation;
import pl.wylegala.room_reservation2.domain.reservation.TimeSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rooms")
public class Room {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    protected Room() {}

    public Room(String name, int capacity){
        this.name = name;
        this.capacity = capacity;
    }

    public boolean isAvailableFor(TimeSlot timeSlot) {
        return reservations.stream()
                .noneMatch(r -> r.getTimeSlot().overlaps(timeSlot));
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public List<Reservation> getReservations() { return reservations; }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }


}
