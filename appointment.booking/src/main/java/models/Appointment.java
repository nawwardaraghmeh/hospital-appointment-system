package models;

import java.util.Objects;
	
public class Appointment {

    private String id;
    private String patientName;
    private TimeSlot timeSlot;

    public Appointment() {
    }

    public Appointment(String id, String patientName, TimeSlot timeSlot) {
        this.id = id;
        this.patientName = patientName;
        this.timeSlot = timeSlot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Appointment{id='%s', patient='%s', timeSlot='%s'}",
                id, patientName, timeSlot != null ? timeSlot.getId() : "null");
    }
}