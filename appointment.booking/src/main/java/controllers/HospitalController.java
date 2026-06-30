package controllers;

import models.Appointment;
import models.TimeSlot;
import repositories.AppointmentRepository;
import repositories.TimeSlotRepository;

import java.util.Collections;
import java.util.List;

public class HospitalController {

    private final TimeSlotRepository timeSlotRepository;
    private final AppointmentRepository appointmentRepository;

    public HospitalController(TimeSlotRepository timeSlotRepository, 
                              AppointmentRepository appointmentRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.appointmentRepository = appointmentRepository;
    }
}