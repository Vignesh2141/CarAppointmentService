package com.example.car_service_appointment.repository;

import com.example.car_service_appointment.entity.Appointment;
import com.example.car_service_appointment.entity.BookedSlots;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Repository
public class SlotRepository {

    private final MongoTemplate mongoTemplate;
    @Autowired
    public  SlotRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate=mongoTemplate;
    }

    public List<Appointment> getApplications(){
        return mongoTemplate.findAll(Appointment.class);
    }

    public Appointment getApplication(String id){
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Appointment.class);
    }

    public void save(Object application){
        mongoTemplate.save(application);
    }

    public BookedSlots getBookedSlots(Date date) throws ParseException {
        Query query=new Query();
        SimpleDateFormat date1=new SimpleDateFormat("yyyy-MM-dd");
        String date2=date1.format(date);
        query.addCriteria(Criteria.where("slotDate").is(date1.parse(date2)));
        return mongoTemplate.findOne(query,BookedSlots.class);
    }

    public void updateSlots(Date date, List<LocalTime> booked) throws ParseException {
        Update update=new Update();
        SimpleDateFormat date1=new SimpleDateFormat("yyyy-MM-dd");
        String date2=date1.format(date);
        Query query=new Query();
        query.addCriteria(Criteria.where("slotDate").is(date1.parse(date2)));
        update.set("slotsBooked",booked);
        mongoTemplate.updateFirst(query,update,BookedSlots.class);
    }

    public void  rescheduleAppointment(Appointment appointment){
        Update update=new Update();
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(appointment.getAppointmentId()));
        update.set("date",appointment.getDate());
        update.set("time",appointment.getTime());
        mongoTemplate.updateFirst(query,update, Appointment.class);
    }

    public void removeAppointment(String id){
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Appointment.class);
    }



}
