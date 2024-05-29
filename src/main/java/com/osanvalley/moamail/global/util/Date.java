package com.osanvalley.moamail.global.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Date {
    public static LocalDateTime parseToLocalDateTime(String dateString) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("[EEE, dd MMM yyyy HH:mm:ss Z '('zzz')']")
                .appendPattern("[EEE, dd MMM yyyy HH:m:s Z '('zzz')']")
                .appendPattern("[EEE, dd MMM yyyy HH:m:ss Z '('zzz')']")
                .appendPattern("[EEE, dd MMM yyyy HH:mm:s Z '('zzz')']")
                .appendPattern("[EEE, dd MMM yyyy H:mm:ss Z '('zzz')']")
                .appendPattern("[EEE, dd MMM yyyy H:m:ss Z '('zzz')']")
                .appendPattern("[EEE, dd MMM yyyy H:mm:s Z '('zzz')']")
                .appendPattern("[EEE, dd MMM yyyy H:m:s Z '('zzz')']")
                .appendPattern("[EEE, d MMM yyyy HH:mm:ss Z '('zzz')']")
                .appendPattern("[EEE, d MMM yyyy HH:m:s Z '('zzz')']")
                .appendPattern("[EEE, d MMM yyyy HH:m:ss Z '('zzz')']")
                .appendPattern("[EEE, d MMM yyyy HH:mm:s Z '('zzz')']")
                .appendPattern("[EEE, d MMM yyyy H:mm:ss Z '('zzz')']")
                .appendPattern("[EEE, d MMM yyyy H:m:ss Z '('zzz')']")
                .appendPattern("[EEE, d MMM yyyy H:mm:s Z '('zzz')']")
                .appendPattern("[EEE, d MMM yyyy H:m:s Z '('zzz')']")
                .appendPattern("[EEE, dd MMM yyyy HH:mm:ss zzz]")
                .appendPattern("[EEE, dd MMM yyyy HH:m:s zzz]")
                .appendPattern("[EEE, dd MMM yyyy HH:m:ss zzz]")
                .appendPattern("[EEE, dd MMM yyyy HH:mm:s zzz]")
                .appendPattern("[EEE, dd MMM yyyy H:mm:ss zzz]")
                .appendPattern("[EEE, dd MMM yyyy H:m:ss zzz]")
                .appendPattern("[EEE, dd MMM yyyy H:mm:s zzz]")
                .appendPattern("[EEE, dd MMM yyyy H:m:s zzz]")
                .appendPattern("[EEE, d MMM yyyy HH:mm:ss zzz]")
                .appendPattern("[EEE, d MMM yyyy HH:m:s zzz]")
                .appendPattern("[EEE, d MMM yyyy HH:m:ss zzz]")
                .appendPattern("[EEE, d MMM yyyy HH:mm:s zzz]")
                .appendPattern("[EEE, d MMM yyyy H:mm:ss zzz]")
                .appendPattern("[EEE, d MMM yyyy H:m:ss zzz]")
                .appendPattern("[EEE, d MMM yyyy H:mm:s zzz]")
                .appendPattern("[EEE, d MMM yyyy H:m:s zzz]")
                .appendPattern("[EEE, dd MMM yyyy HH:mm:ss Z]")
                .appendPattern("[EEE, dd MMM yyyy HH:m:s Z]")
                .appendPattern("[EEE, dd MMM yyyy HH:m:ss Z]")
                .appendPattern("[EEE, dd MMM yyyy HH:mm:s Z]")
                .appendPattern("[EEE, dd MMM yyyy H:mm:ss Z]")
                .appendPattern("[EEE, dd MMM yyyy H:m:ss Z]")
                .appendPattern("[EEE, dd MMM yyyy H:mm:s Z]")
                .appendPattern("[EEE, dd MMM yyyy H:m:s Z]")
                .appendPattern("[EEE, d MMM yyyy HH:mm:ss Z]")
                .appendPattern("[EEE, d MMM yyyy HH:m:s Z]")
                .appendPattern("[EEE, d MMM yyyy HH:m:ss Z]")
                .appendPattern("[EEE, d MMM yyyy HH:mm:s Z]")
                .appendPattern("[EEE, d MMM yyyy H:mm:ss Z]")
                .appendPattern("[EEE, d MMM yyyy H:m:ss Z]")
                .appendPattern("[EEE, d MMM yyyy H:mm:s Z]")
                .appendPattern("[EEE, d MMM yyyy H:m:s Z]")
                .appendPattern("[dd MMM yyyy HH:mm:ss Z]")
                .appendPattern("[dd MMM yyyy HH:m:s Z]")
                .appendPattern("[dd MMM yyyy HH:m:ss Z]")
                .appendPattern("[dd MMM yyyy HH:mm:s Z]")
                .appendPattern("[dd MMM yyyy H:mm:ss Z]")
                .appendPattern("[dd MMM yyyy H:m:ss Z]")
                .appendPattern("[dd MMM yyyy H:mm:s Z]")
                .appendPattern("[dd MMM yyyy H:m:s Z]")
                .appendPattern("[d MMM yyyy HH:mm:ss Z]")
                .appendPattern("[d MMM yyyy HH:m:s Z]")
                .appendPattern("[d MMM yyyy HH:m:ss Z]")
                .appendPattern("[d MMM yyyy HH:mm:s Z]")
                .appendPattern("[d MMM yyyy H:mm:ss Z]")
                .appendPattern("[d MMM yyyy H:m:ss Z]")
                .appendPattern("[d MMM yyyy H:mm:s Z]")
                .appendPattern("[d MMM yyyy H:m:s Z]")
                .toFormatter();

        // 문자열을 ZonedDateTime 객체로 파싱
        ZonedDateTime utcTime = ZonedDateTime.parse(dateString, formatter);

        // 서울 시간대(KST)로 변환하고 LocalDateTime으로 변환
        return utcTime.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }
}
