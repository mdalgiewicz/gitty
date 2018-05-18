package pl.dalgim.gitty.common.date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Mateusz Dalgiewicz
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  private static final String EUROPE_WARSAW = "Europe/Warsaw";

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx)
      throws IOException {
    String date = p.getText();
    if (date == null) {
      return null;
    } else if (date.length() > 10 && date.charAt(10) == 'T' && date.endsWith("Z")) {
      Instant parse = Instant.parse(date);
      return LocalDateTime.ofInstant(parse, ZoneId.of(EUROPE_WARSAW));
    }
    return LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }
}
