package pl.wylegala.room_reservation2.domain.reservation;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class TimeSlot {
     private LocalDateTime startTime;
     private LocalDateTime endTime;

     protected TimeSlot() {}


    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        Objects.requireNonNull(startTime,"startTime nie może być null");
        Objects.requireNonNull(endTime,"endTime nie może być null");
        if (startTime.isAfter(endTime))
            throw new IllegalArgumentException("'startTime' musi być przed 'endTime'");
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean overlaps(TimeSlot other){
        return this.startTime.isBefore(other.endTime)
                && other.startTime.isBefore(this.endTime);
    }

    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}
