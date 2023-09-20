package com.example.car_service_appointment.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {

    private final SimpleDateFormat dateFormat;

    public DateSerializer(){
       dateFormat= new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedDate = dateFormat.format(value);
        gen.writeString(formattedDate);
    }
}
