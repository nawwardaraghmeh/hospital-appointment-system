package repositories;

import models.TimeSlot;

import java.util.List;

public interface TimeSlotRepository {
    List<TimeSlot> findAll();
    TimeSlot findById(String id); 
    List<TimeSlot> findByDepartment(String department);  
    void save(TimeSlot timeSlot);
    void delete(String id);
}