package Tools;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class TimeLogging {
    LocalTime first;
    LocalTime second;

    public LocalTime getDiff() {
        return second
                .minusMinutes(first.getMinute())
                .minusSeconds(first.getSecond())
                .minusNanos(first.getNano());
    }
}
