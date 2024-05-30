package com.osanvalley.moamail.global.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        LocalDate today = LocalDate.now();
        DateTimeFormatter todayFormatter = DateTimeFormatter.ofPattern("a hh:mm");
        DateTimeFormatter otherDayFormatter = DateTimeFormatter.ofPattern("MM.dd HH:mm");

        String formattedDate;
        if (value.toLocalDate().isEqual(today)) {
            formattedDate = value.format(todayFormatter);
        } else {
            formattedDate = value.format(otherDayFormatter);
        }

        gen.writeString(formattedDate);
    }
}
