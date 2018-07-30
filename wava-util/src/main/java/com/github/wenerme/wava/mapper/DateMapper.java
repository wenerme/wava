package com.github.wenerme.wava.mapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.Date;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/5/17
 */
public interface DateMapper {

  static Date parseDate(CharSequence s, DateTimeFormatter formatter) {
    return toDate(formatter.parse(s));
  }

  static Date toDate(TemporalAccessor t) {
    if (t == null) {
      return null;
    }
    if (t.isSupported(ChronoField.NANO_OF_SECOND) && t.isSupported(ChronoField.INSTANT_SECONDS)) {
      long nanos = t.getLong(ChronoField.NANO_OF_SECOND);
      long epochSeconds = t.getLong(ChronoField.INSTANT_SECONDS);
      return Date.from(Instant.ofEpochSecond(epochSeconds, nanos));
    }
    if (t.query(TemporalQueries.zone()) == null) {
      if (t.query(TemporalQueries.localTime()) == null) {
        return toDate(LocalDate.from(t));
      } else {
        return toDate(LocalDateTime.from(t));
      }
    } else {
      return toDate(ZonedDateTime.from(t));
    }
  }

  static Date toDate(ZonedDateTime s) {
    if (s == null) {
      return null;
    }
    return Date.from(s.toInstant());
  }

  static Date toDate(LocalDateTime s) {
    if (s == null) {
      return null;
    }

    return Date.from(s.atZone(ZoneId.systemDefault()).toInstant());
  }

  static LocalDateTime toLocalDateTime(Date s) {
    if (s == null) {
      return null;
    }
    return LocalDateTime.from(s.toInstant().atZone(ZoneId.systemDefault()));
  }

  static Date toDate(LocalDate s) {
    if (s == null) {
      return null;
    }
    return Date.from(s.atStartOfDay().toInstant(ZoneOffset.UTC));
  }
}
