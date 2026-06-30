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
import views.HospitalView;

@RunWith(MockitoJUnitRunner.class)
public class HospitalControllerTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private AppointmentRepository appointmentRepository;
    
    @Mock
    private HospitalView view;

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

        controller.getAllTimeSlots();

        verify(timeSlotRepository, times(1)).findAll();
        verify(view, times(1)).showAllTimeSlots(expectedSlots);
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

        controller.getAvailableTimeSlots();

        verify(timeSlotRepository, times(1)).findAll();
        verify(view, times(1)).showAvailableTimeSlots(Arrays.asList(testTimeSlot));
    }
    
    @Test
    public void testGetAllAppointments() {
        List<Appointment> expectedAppointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findAll()).thenReturn(expectedAppointments);

        controller.getAllAppointments();

        verify(appointmentRepository, times(1)).findAll();
        verify(view, times(1)).showAllAppointments(expectedAppointments);
    }
    
    @Test
    public void testCreateAppointmentSuccess() {
        when(timeSlotRepository.findById("TS001")).thenReturn(testTimeSlot);
        doNothing().when(appointmentRepository).save(testAppointment);

        controller.createAppointment(testAppointment);

        verify(timeSlotRepository, times(1)).findById("TS001");
        verify(appointmentRepository, times(1)).save(testAppointment);
        verify(view, times(1)).appointmentCreated(testAppointment);
    }
    
    @Test
    public void testCreateAppointmentWhenTimeSlotNotFound() {
        when(timeSlotRepository.findById("TS001")).thenReturn(null);

        controller.createAppointment(testAppointment);

        verify(appointmentRepository, never()).save(testAppointment);
        verify(view, times(1)).showError("Time slot not found: TS001");
    }
    
    @Test
    public void testCreateAppointmentWhenTimeSlotAlreadyBooked() {
        TimeSlot bookedSlot = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        bookedSlot.setAppointment(new Appointment("APT002", "Jane Smith", bookedSlot));
        
        when(timeSlotRepository.findById("TS001")).thenReturn(bookedSlot);

        controller.createAppointment(testAppointment);

        verify(appointmentRepository, never()).save(testAppointment);
        verify(view, times(1)).showError("Time slot is already booked");
    }
    
    @Test
    public void testDeleteAppointmentSuccess() {
        when(appointmentRepository.findById("APT001")).thenReturn(testAppointment);

        controller.deleteAppointment("APT001");

        verify(appointmentRepository, times(1)).delete("APT001");
        verify(view, times(1)).appointmentDeleted("APT001");
    }
    
    @Test
    public void testDeleteAppointmentWhenNotFound() {
        when(appointmentRepository.findById("APT001")).thenReturn(null);

        controller.deleteAppointment("APT001");

        verify(appointmentRepository, never()).delete("APT001");
        verify(view, times(1)).showError("Appointment not found: APT001");
    }
}