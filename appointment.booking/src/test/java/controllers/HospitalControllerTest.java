package controllers;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import models.Appointment;
import models.TimeSlot;
import repositories.AppointmentRepository;
import repositories.TimeSlotRepository;

@RunWith(MockitoJUnitRunner.class)
public class HospitalControllerTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private HospitalController controller;

    private TimeSlot testTimeSlot;
    private Appointment testAppointment;

    @Before
    public void setUp() {
        testTimeSlot = new TimeSlot(
            "TS001",
            "Dr. House",
            "Cardiology",
            "Room 101",
            LocalDateTime.now().plusDays(1)
        );
        
        testAppointment = new Appointment(
            "APT001",
            "John Doe",
            testTimeSlot
        );
    }
}