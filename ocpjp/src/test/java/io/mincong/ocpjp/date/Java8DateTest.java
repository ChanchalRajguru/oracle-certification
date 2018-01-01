package io.mincong.ocpjp.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.function.Function;
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
      fail();
    } catch (UnsupportedTemporalTypeException e) {
      // ok
    }
  }

  @Test
  public void localDate_manipulateValues() throws Exception {
    LocalDate d = LocalDate.of(2018, 1, 2);
    assertThat(d.withYear(2017)).isEqualTo(LocalDate.of(2017, 1, 2));
    assertThat(d.withMonth(2)).isEqualTo(LocalDate.of(2018, 2, 2));
    assertThat(d.withDayOfMonth(3)).isEqualTo(LocalDate.of(2018, 1, 3));
    assertThat(d.withDayOfYear(4)).isEqualTo(LocalDate.of(2018, 1, 4));
  }

  @Test
  public void localDate_temporalAdjusters() throws Exception {
    LocalDate d = LocalDate.of(2018, 1, 2);
    LocalDate f = d.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
    LocalDate l = d.with(TemporalAdjusters.lastDayOfMonth());
    assertThat(f).isEqualTo(LocalDate.of(2018, 1, 5));
    assertThat(l).isEqualTo(LocalDate.of(2018, 1, 31));
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
    Stream.of(dt1, dt2, dt3).forEach(dt -> assertEquals(expected, dt));
  }

  @Test
  public void duration_betweenTwoLocalDateTimes() throws Exception {
    LocalDateTime d1 = LocalDateTime.of(2018, 1, 1, 0, 0, 0);
    LocalDateTime d2 = LocalDateTime.of(2018, 1, 1, 0, 12, 0);
    assertThat(Duration.between(d1, d2)).isEqualTo(Duration.ofMinutes(12));
  }

  @Test
  public void period_againstDuration() throws Exception {
    ZonedDateTime d = ZonedDateTime.of(2017, 10, 29, 0, 0, 0, 0, ZoneId.of("Europe/Paris"));
    Function<String, ZonedDateTime> parse = ZonedDateTime::parse;
    /*
     * Durations and periods differ in their treatment of daylight
     * savings time when added to `ZonedDateTime`. A `Duration` will
     * add an exact number of seconds, thus a duration of one day is
     * always exactly 24 hours. By contrast, a `Period` will add a
     * conceptual day, trying to maintain the local time.
     */
    assertThat(d.plus(Period.ofDays(1))).isEqualTo(parse.apply("2017-10-30T00:00:00+01:00"));
    assertThat(d.plus(Duration.ofDays(1))).isEqualTo(parse.apply("2017-10-29T23:00:00+01:00"));
  }

  @Test
  public void period_betweenTwoLocalDates() throws Exception {
    LocalDate d1 = LocalDate.of(2018, 1, 1);
    LocalDate d2 = LocalDate.of(2018, 1, 2);
    assertThat(Period.between(d1, d2)).isEqualTo(Period.ofDays(1));
  }

}
