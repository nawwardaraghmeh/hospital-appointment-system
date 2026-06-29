package repositories;

import models.TimeSlot;

import java.util.List;

public interface TimeSlotRepository {
    List<TimeSlot> findAll();
}