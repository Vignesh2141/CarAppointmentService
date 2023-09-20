package com.example.car_service_appointment.controller;

import com.example.car_service_appointment.entity.Appointment;
import com.example.car_service_appointment.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class ApplicationController {
    private final SlotService slotService;

    @Autowired
    public ApplicationController(SlotService slotService){
        this.slotService=slotService;
    }

    @GetMapping
    public List<Appointment> getAllApplication(){
        return slotService.getAllApplication();
    }

    @GetMapping("/{id}")
    public Appointment getApplicationById(@PathVariable String id){
        return slotService.getApplication(id);
    }

    @DeleteMapping("/cancel/{id}")
    public String  cancelAppointment(@PathVariable String id) throws ParseException {
        return slotService.cancelAppointment(id);
    }

    @PutMapping("reschedule")
    public Appointment rescheduleAppointment(@RequestBody Appointment appointment) throws ParseException {
        return slotService.reschedule(appointment);
    }
}
