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
    
    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository.findAll();
    }
    
    public TimeSlot getTimeSlotById(String id) {
        return timeSlotRepository.findById(id);
    }
    
    public List<TimeSlot> getAvailableTimeSlots() {
        List<TimeSlot> allSlots = timeSlotRepository.findAll();
        return allSlots.stream()
                .filter(slot -> slot.getAppointment() == null)
                .collect(java.util.stream.Collectors.toList());
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public Appointment createAppointment(Appointment appointment) {
    	    timeSlotRepository.findById(appointment.getTimeSlot().getId());  
    	    appointmentRepository.save(appointment);
    	    return appointment;
    }
}