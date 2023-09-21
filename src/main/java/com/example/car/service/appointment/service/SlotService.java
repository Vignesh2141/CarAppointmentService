package com.example.car.service.appointment.service;

import com.example.car.service.appointment.entity.Appointment;
import com.example.car.service.appointment.entity.AvailableSlots;
import com.example.car.service.appointment.entity.BookedSlots;
import com.example.car.service.appointment.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
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
            if (optionalBookedSlots.equals(Optional.empty())) {
                slotRepository.save(
                        BookedSlots.builder().slotDate(date).build()
                );
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

        BookedSlots slots=slotRepository.getBookedSlots(date);
        List<LocalTime> slotTime=slots.getSlotsBooked();
        List<LocalTime> possibleSlots = generatePossibleSlots(startTime, endTime);
        Optional<BookedSlots> optionalSlots = Optional.of(slots);

        List<LocalTime> availableSlots = optionalSlots.map(slotsList -> {
            List<LocalTime> slotsCopy = new ArrayList<>(possibleSlots);
            slotsCopy.removeAll(slotTime);
            return slotsCopy;
        }).orElse(possibleSlots);

        return AvailableSlots.builder().slotsAvailable(availableSlots).build();

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
