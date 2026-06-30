package repositories;

import java.util.List;
import models.Appointment;

public interface AppointmentRepository {
    List<Appointment> findAll();
    Appointment findById(String id); 
    void save (Appointment appointment);
    void delete(String id);
}