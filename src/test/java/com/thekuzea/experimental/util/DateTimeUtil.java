package com.thekuzea.experimental.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtil {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static String convertOffsetDateTimeToString(final OffsetDateTime offsetDateTime) {
        return offsetDateTime.format(FORMATTER);
    }
}
