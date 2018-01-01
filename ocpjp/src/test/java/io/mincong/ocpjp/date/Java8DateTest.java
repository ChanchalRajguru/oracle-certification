package io.mincong.ocpjp.date;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * Java 8 in Action, Chapter 12: New Date and Time API
 *
 * @author Mincong Huang
 */
public class Java8DateTest {

  @Test
  public void localDate_readingValues() throws Exception {
    LocalDate d = LocalDate.of(2018, 1, 2);
    assertThat(d.getYear()).isEqualTo(2018);
    assertThat(d.getDayOfMonth()).isEqualTo(2);
    assertThat(d.getDayOfWeek()).isEqualTo(DayOfWeek.TUESDAY);
    assertThat(d.lengthOfMonth()).isEqualTo(31);
    assertThat(d.lengthOfYear()).isEqualTo(365);
  }

  @Test
  public void localDate_constructWithMonth() throws Exception {
    LocalDate d = LocalDate.of(2018, Month.JANUARY, 2);
    assertThat(d).isEqualTo(LocalDate.of(2018, 1, 2));
  }

  @Test
  public void localDate_temporalField() throws Exception {
    LocalDate d = LocalDate.of(2018, 1, 2);
    assertThat(d.get(ChronoField.YEAR)).isEqualTo(2018);
    assertThat(d.get(ChronoField.DAY_OF_MONTH)).isEqualTo(2);
    assertThat(d.get(ChronoField.DAY_OF_WEEK)).isEqualTo(DayOfWeek.TUESDAY.getValue());
    try {
      d.get(ChronoField.HOUR_OF_DAY);
    } catch (UnsupportedTemporalTypeException e) {
      // ok
    }
  }

  @Test
  public void localDate_parse() throws Exception {
    LocalDate d = LocalDate.parse("2018-01-02");
    assertThat(d).isEqualTo(LocalDate.of(2018, 1, 2));
  }

  @Test
  public void localTime_readingValues() throws Exception {
    LocalTime t = LocalTime.of(11, 38, 0);
    assertThat(t.getHour()).isEqualTo(11);
    assertThat(t.getMinute()).isEqualTo(38);
    assertThat(t.getSecond()).isEqualTo(0);
  }

  @Test
  public void localTime_parse() throws Exception {
    LocalTime t = LocalTime.parse("13:45:20");
    assertThat(t).isEqualTo(LocalTime.of(13, 45, 20));
  }

  @Test
  public void localDateTime_combineDateAndTime() throws Exception {
    LocalDate d = LocalDate.of(2018, 1, 2);
    LocalDateTime dt1 = d.atTime(13, 38);
    LocalDateTime dt2 = d.atTime(13, 38, 0);
    LocalDateTime dt3 = d.atTime(LocalTime.of(13, 38, 0));

    LocalDateTime expected = LocalDateTime.of(2018, 1, 2, 13, 38, 0);
    Stream.of(dt1, dt2, dt3)
        .forEach(dt -> assertThat(dt).isEqualTo(expected));
  }

}
