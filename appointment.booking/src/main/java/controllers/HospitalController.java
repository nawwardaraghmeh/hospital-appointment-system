package controllers;

import models.Appointment;
import models.TimeSlot;
import repositories.AppointmentRepository;
import repositories.TimeSlotRepository;
import views.HospitalView;

import java.util.Collections;
import java.util.List;

public class HospitalController {

    private final TimeSlotRepository timeSlotRepository;
    private final AppointmentRepository appointmentRepository;
    private final HospitalView view;

    public HospitalController(TimeSlotRepository timeSlotRepository, 
                              AppointmentRepository appointmentRepository,
                              HospitalView view) {	
        this.timeSlotRepository = timeSlotRepository;
        this.appointmentRepository = appointmentRepository;
        this.view = view;
    }
   
    public void getAllTimeSlots() {
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();
        view.showAllTimeSlots(timeSlots);
    }
    
    public TimeSlot getTimeSlotById(String id) {
        return timeSlotRepository.findById(id);
    }
    
    public void getAvailableTimeSlots() {
        List<TimeSlot> allSlots = timeSlotRepository.findAll();
        List<TimeSlot> availableSlots = allSlots.stream()
                .filter(slot -> slot.getAppointment() == null)
                .collect(java.util.stream.Collectors.toList());
        view.showAvailableTimeSlots(availableSlots);
    }
    
    public void getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        view.showAllAppointments(appointments);
    }
    
    public void createAppointment(Appointment appointment) {
        TimeSlot timeSlot = timeSlotRepository.findById(appointment.getTimeSlot().getId());
        
        if (timeSlot == null) {
            view.showError("Time slot not found: " + appointment.getTimeSlot().getId());
            return;
        }
        
        if (timeSlot.getAppointment() != null) {
            view.showError("Time slot is already booked");
            return;
        }
        
        appointmentRepository.save(appointment);
        view.appointmentCreated(appointment);
    }
    
    public void deleteAppointment(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId);
        
        if (appointment == null) {
            view.showError("Appointment not found: " + appointmentId);
            return;
        }
        
        appointmentRepository.delete(appointmentId);
        view.appointmentDeleted(appointmentId);
    }
    
    public void addTimeSlot(TimeSlot timeSlot) {
        timeSlotRepository.save(timeSlot);
    }

}