package com.example.car_service_appointment.service;

import com.example.car_service_appointment.entity.Appointment;
import com.example.car_service_appointment.entity.AvailableSlots;
import com.example.car_service_appointment.entity.BookedSlots;
import com.example.car_service_appointment.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    @Autowired
    public SlotService(SlotRepository slotRepository){
        this.slotRepository=slotRepository;
    }

    public List<Appointment> getAllApplication(){
        return slotRepository.getApplications();
    }

    public Appointment getApplication(String id){
        return slotRepository.getApplication(id);
    }

    public String bookSlot(Appointment application) throws ParseException {
        Date date = application.getDate();
        Optional<BookedSlots> optionalBookedSlots = Optional.ofNullable(slotRepository.getBookedSlots(date));
        List<LocalTime> booked = optionalBookedSlots.map(BookedSlots::getSlotsBooked).orElse(new ArrayList<>());

        boolean isSlotBooked = booked.stream().anyMatch(book -> book.equals(application.getTime()));

        if (!isSlotBooked) {
            if (optionalBookedSlots.isEmpty()) {
                BookedSlots newBookedSlots = new BookedSlots();
                newBookedSlots.setSlotDate(date);
                slotRepository.save(newBookedSlots);
            }
            booked.add(application.getTime());
            slotRepository.save(application);
            slotRepository.updateSlots(date, booked);
            return "Slot is booked successfully";
        } else {
            return "Slot is not available; it's already booked";
        }

    }

    public AvailableSlots getAvailableSlots(Date date) throws ParseException {
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 0);
        AvailableSlots availableSlots1=new AvailableSlots();
        BookedSlots slots=slotRepository.getBookedSlots(date);
        List<LocalTime> slotTime=slots.getSlotsBooked();
        List<LocalTime> possibleSlots = generatePossibleSlots(startTime, endTime);
        Optional<BookedSlots> optionalSlots = Optional.of(slots);
        List<LocalTime> availableSlots = optionalSlots.map(slotsList -> {
            List<LocalTime> slotsCopy = new ArrayList<>(possibleSlots);
            slotsCopy.removeAll(slotTime);
            return slotsCopy;
        }).orElse(possibleSlots);

        availableSlots1.setSlotsAvailable(availableSlots);
        return availableSlots1;
    }

    private List<LocalTime> generatePossibleSlots(LocalTime startTime, LocalTime endTime) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime currentSlot = startTime;
        while (!currentSlot.isAfter(endTime)) {
            slots.add(currentSlot);
            currentSlot = currentSlot.plusMinutes(60);
            if (currentSlot.equals(endTime)) {
                break;
            }
        }
        return slots;
    }

    public void addSlots(BookedSlots bookedSlots) throws ParseException {
        SimpleDateFormat date1=new SimpleDateFormat("yyyy-MM-dd");
        String  applicationDate=date1.format(bookedSlots.getSlotDate());
        bookedSlots.setSlotDate(date1.parse(applicationDate));
        slotRepository.save(bookedSlots);
    }

    public String cancelAppointment(String id) throws ParseException {
        Appointment appointment=slotRepository.getApplication(id);
        BookedSlots bookedSlots=slotRepository.getBookedSlots(appointment.getDate());
        bookedSlots.getSlotsBooked().remove(appointment.getTime());
        slotRepository.updateSlots(appointment.getDate(),bookedSlots.getSlotsBooked());
        slotRepository.removeAppointment(appointment.getAppointmentId());
        return "Slot canceled";
    }

    public Appointment reschedule(Appointment app) throws ParseException {
        Appointment appointment=slotRepository.getApplication(app.getAppointmentId());
        BookedSlots bookedSlots=slotRepository.getBookedSlots(app.getDate());
        bookedSlots.getSlotsBooked().remove(appointment.getTime());
        appointment.setDate(app.getDate());
        appointment.setTime(app.getTime());
        bookedSlots.getSlotsBooked().add(app.getTime());
        slotRepository.updateSlots(app.getDate(),bookedSlots.getSlotsBooked());
        slotRepository.rescheduleAppointment(appointment);
        return appointment;

    }

}
