package com.example.car_service_appointment.controller;

import com.example.car_service_appointment.entity.Appointment;
import com.example.car_service_appointment.entity.AvailableSlots;
import com.example.car_service_appointment.entity.BookedSlots;
import com.example.car_service_appointment.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/slot")
public class SlotController {

    private final SlotService slotService;
    @Autowired
    public SlotController(SlotService slotService){
        this.slotService=slotService;
    }


    @GetMapping("/{date}")
    public AvailableSlots getAvailableSlot(@PathVariable String  date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1=dateFormat.parse(date);
        return slotService.getAvailableSlots(date1);
    }

    @PostMapping("/add")
    public void addSlots(@RequestBody BookedSlots slots) throws ParseException {
        slotService.addSlots(slots);
    }

    @PostMapping("/book")
    public String  bookSlot(@RequestBody Appointment application) throws ParseException {
        return slotService.bookSlot(application);
    }

}
