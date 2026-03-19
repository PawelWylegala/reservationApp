package pl.wylegala.room_reservation2.domain.reservation;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.*;

class TimeSlotTest {

    @Test
    void powinienWykrycNakladajaceSieTerminy() {
        TimeSlot slotA = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 10, 0),
                LocalDateTime.of(2026, 3, 20, 12, 0)
        );
        TimeSlot slotB = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 11, 0),
                LocalDateTime.of(2026, 3, 20, 13, 0)
        );

        assertThat(slotA.overlaps(slotB)).isTrue();
    }

    @Test
    void powinienZezwolicNaRezerwacjePoKolei() {
        TimeSlot slotA = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 10, 0),
                LocalDateTime.of(2026, 3, 20, 12, 0)
        );
        TimeSlot slotB = new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 12, 0),
                LocalDateTime.of(2026, 3, 20, 14, 0)
        );

        assertThat(slotA.overlaps(slotB)).isFalse();
    }

    @Test
    void powinienRzucicWyjatekGdyStartPoPoczatku() {
        assertThatThrownBy(() -> new TimeSlot(
                LocalDateTime.of(2026, 3, 20, 14, 0),
                LocalDateTime.of(2026, 3, 20, 10, 0)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("'startTime' musi być przed 'endTime'");
    }

    @Test
    void powninienRzucicWyjatekGdyStartToNull(){


    }
}