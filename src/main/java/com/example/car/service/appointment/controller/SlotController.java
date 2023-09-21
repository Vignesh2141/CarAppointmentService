package com.example.car.service.appointment.controller;

import com.example.car.service.appointment.config.ConstantObjects;
import com.example.car.service.appointment.entity.Appointment;
import com.example.car.service.appointment.entity.AvailableSlots;
import com.example.car.service.appointment.service.SlotService;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/slot")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService){
        this.slotService=slotService;
    }

    @GetMapping("/{date}")
    public AvailableSlots getAvailableSlot(@PathVariable String  date) throws ParseException {
        return slotService.getAvailableSlots(ConstantObjects.getDateFormat().parse(date));
    }

    @PostMapping("/book")
    public String  bookSlot(@RequestBody Appointment application) throws ParseException {
        return slotService.bookSlot(application);
    }

}
