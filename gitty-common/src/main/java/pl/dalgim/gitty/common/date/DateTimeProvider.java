package pl.dalgim.gitty.common.date;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Class for stubbing date in unit tests.
 * @author Mateusz Dalgiewicz
 */
public class DateTimeProvider {

  private static final ZoneId zoneId = ZoneId.of("Europe/Warsaw");
  private static Clock clock = Clock.system(zoneId);

  public synchronized static void useFixedClock(LocalDateTime dateTime) {
    clock = Clock.fixed(dateTime.atZone(zoneId).toInstant(), zoneId);
  }

  public static LocalDate currentLocalDate() {
    return LocalDate.now(clock);
  }

  public static LocalDateTime currentLocalDateTime() {
    return LocalDateTime.now(clock);
  }

}
