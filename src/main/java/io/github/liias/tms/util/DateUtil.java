package io.github.liias.tms.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static java.util.Optional.ofNullable;

public class DateUtil {
    public static LocalDateTime toLocalDateTime(Date date) {
        return ofNullable(date)
                .map(d -> LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()))
                .orElse(null);
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return ofNullable(localDateTime)
                .map(ldt -> Timestamp.valueOf(localDateTime))
                .orElse(null);
    }
}
