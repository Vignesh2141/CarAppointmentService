package com.example.car.service.appointment.config;


import java.text.SimpleDateFormat;



public class ConstantObjects {
    private static SimpleDateFormat dateFormat = null;
    private ConstantObjects() {
    }
    public static SimpleDateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        return dateFormat;
    }


}
