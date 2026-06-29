package models;

import java.time.LocalDateTime;
import java.util.Objects;

public class TimeSlot {

    private String id;
    private String doctorName;
    private String department;
    private String roomNumber;
    private LocalDateTime appointmentDateTime;
    private Appointment appointment;

    public TimeSlot() {
    }

    public TimeSlot(String id, String doctorName, String department, 
                    String roomNumber, LocalDateTime appointmentDateTime) {
        this.id = id;
        this.doctorName = doctorName;
        this.department = department;
        this.roomNumber = roomNumber;
        this.appointmentDateTime = appointmentDateTime;
        this.appointment = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(id, timeSlot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("TimeSlot{id='%s', doctor='%s', dept='%s', room='%s', time='%s'}",
                id, doctorName, department, roomNumber, appointmentDateTime);
    }
}