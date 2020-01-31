package de.novatec.ibmbpm.swaggerintegrationdemo.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateConversionUtil {

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return convertDateToLocalDateTime(date, ZoneId.systemDefault());
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date, ZoneId timeZone) {
        return date.toInstant()
                .atZone(timeZone)
                .toLocalDateTime();
    }
}
