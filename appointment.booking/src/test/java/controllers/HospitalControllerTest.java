package controllers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
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
    
    @Test
    public void testGetAllTimeSlots() {
        List<TimeSlot> expectedSlots = Arrays.asList(testTimeSlot);
        when(timeSlotRepository.findAll()).thenReturn(expectedSlots);

        List<TimeSlot> result = controller.getAllTimeSlots();

        assertThat(result).isEqualTo(expectedSlots);
        verify(timeSlotRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetTimeSlotById() {
        when(timeSlotRepository.findById("TS001")).thenReturn(testTimeSlot);

        TimeSlot result = controller.getTimeSlotById("TS001");

        assertThat(result).isEqualTo(testTimeSlot);
        verify(timeSlotRepository, times(1)).findById("TS001");
    }
    
    @Test
    public void testGetAvailableTimeSlots() {
        TimeSlot bookedSlot = new TimeSlot("TS002", "Dr. Smith", "Neurology", "Room 202", LocalDateTime.now().plusDays(2));
        bookedSlot.setAppointment(new Appointment("APT002", "Jane Smith", bookedSlot));
        
        List<TimeSlot> allSlots = Arrays.asList(testTimeSlot, bookedSlot);
        when(timeSlotRepository.findAll()).thenReturn(allSlots);

        List<TimeSlot> availableSlots = controller.getAvailableTimeSlots();

        assertThat(availableSlots).hasSize(1);
        assertThat(availableSlots.get(0)).isEqualTo(testTimeSlot);
        verify(timeSlotRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetAllAppointments() {
        List<Appointment> expectedAppointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findAll()).thenReturn(expectedAppointments);

        List<Appointment> result = controller.getAllAppointments();

        assertThat(result).isEqualTo(expectedAppointments);
        verify(appointmentRepository, times(1)).findAll();
    }
    
    @Test
    public void testCreateAppointmentSuccess() {
        when(timeSlotRepository.findById("TS001")).thenReturn(testTimeSlot);
        doNothing().when(appointmentRepository).save(testAppointment);

        Appointment result = controller.createAppointment(testAppointment);

        assertThat(result).isEqualTo(testAppointment);
        verify(timeSlotRepository, times(1)).findById("TS001");
        verify(appointmentRepository, times(1)).save(testAppointment);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateAppointmentWhenTimeSlotNotFound() {
        when(timeSlotRepository.findById("TS001")).thenReturn(null);

        controller.createAppointment(testAppointment);

        verify(appointmentRepository, never()).save(testAppointment);
    }
}