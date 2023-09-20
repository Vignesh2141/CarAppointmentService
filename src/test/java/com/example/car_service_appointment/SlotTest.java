package com.example.car_service_appointment;

import com.example.car_service_appointment.controller.ApplicationController;
import com.example.car_service_appointment.entity.Appointment;
import com.example.car_service_appointment.service.SlotService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SlotTest {

    @InjectMocks
    private ApplicationController applicationController;

    @Mock
    private SlotService slotService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllApplications(){
        List<Appointment> applications= Arrays.asList(
                new Appointment("1","serviceOperator1","Vignesh",new Date(1924867200000L), LocalTime.of(10,0,0)),
                new Appointment("2","serviceOperator1","Vignesh",new Date(1924867200000L), LocalTime.of(10,0,0))
        );
        when(slotService.getAllApplication()).thenReturn(applications);
        List<Appointment> result=slotService.getAllApplication();
        assertEquals(applications.size(),result.size());
        assertNotEquals(result.get(0).getAppointmentId(),result.get(1).getAppointmentId());
    }



}
