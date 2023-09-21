package com.example.car.service.appointment.entity;

import com.example.car.service.appointment.config.ConstantObjects;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class DateDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser p, DeserializationContext context) throws IOException {
        try {
            return ConstantObjects.getDateFormat().parse(p.getValueAsString());
        } catch (ParseException e) {
            throw new IOException("Error parsing date", e);
        }
    }
}


