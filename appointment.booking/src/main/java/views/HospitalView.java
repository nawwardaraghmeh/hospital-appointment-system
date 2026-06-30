package views;

import models.Appointment;
import models.TimeSlot;
import java.util.List;

public interface HospitalView {
    void showAllTimeSlots(List<TimeSlot> timeSlots);
    void showAllAppointments(List<Appointment> appointments);
    void showAvailableTimeSlots(List<TimeSlot> timeSlots);
    void showError(String message);
    void appointmentCreated(Appointment appointment);
    void appointmentDeleted(String appointmentId);
}