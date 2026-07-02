package views;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import controllers.HospitalController;
import models.Appointment;
import models.TimeSlot;
import repositories.AppointmentRepository;
import repositories.TimeSlotRepository;

@RunWith(GUITestRunner.class)
public class HospitalSwingViewTest extends AssertJSwingJUnitTestCase {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private HospitalController controller;

    private FrameFixture window;
    private HospitalSwingView view;

    @Override
    protected void onSetUp() {
    		MockitoAnnotations.openMocks(this);
    	
        view = GuiActionRunner.execute(() -> new HospitalSwingView());
        
        controller = new HospitalController(timeSlotRepository, appointmentRepository, view);
        
        view.setHospitalController(controller);
        
        window = new FrameFixture(robot(), view);
        window.show();
    }

    @Override
    protected void onTearDown() {
        window.cleanUp();
    }
    
    @Test
    public void testWindowInitialState() {
        assertThat(window.target().getTitle()).isEqualTo("Hospital Appointment System");
        
        window.list("timeSlotList");
        window.list("appointmentList");
        window.textBox("patientNameTextBox");
        window.button("bookButton");       
        window.button("refreshButton");    
        window.button("deleteButton");    
        window.label("errorLabel");  
    }
    
    @Test
    public void testShowAllTimeSlots() {
        TimeSlot slot1 = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        TimeSlot slot2 = new TimeSlot("TS002", "Dr. Smith", "Neurology", "Room 202", LocalDateTime.now().plusDays(2));
        List<TimeSlot> timeSlots = Arrays.asList(slot1, slot2);

        view.showAllTimeSlots(timeSlots);

        String[] listContents = window.list("timeSlotList").contents();
        assertThat(listContents).containsExactly(slot1.toString(), slot2.toString());
    }
    
    @Test
    public void testShowAvailableTimeSlots() {
        TimeSlot slot1 = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        TimeSlot slot2 = new TimeSlot("TS002", "Dr. Smith", "Neurology", "Room 202", LocalDateTime.now().plusDays(2));
        List<TimeSlot> availableSlots = Arrays.asList(slot1);

        view.showAvailableTimeSlots(availableSlots);

        String[] listContents = window.list("timeSlotList").contents();
        assertThat(listContents).containsExactly(slot1.toString());
        assertThat(listContents).doesNotContain(slot2.toString());
    }
    
    @Test
    public void testShowAllAppointments() {
        TimeSlot slot = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        Appointment apt1 = new Appointment("APT001", "John Doe", slot);
        Appointment apt2 = new Appointment("APT002", "Jane Smith", slot);
        List<Appointment> appointments = Arrays.asList(apt1, apt2);

        view.showAllAppointments(appointments);

        String[] listContents = window.list("appointmentList").contents();
        assertThat(listContents).containsExactly(apt1.toString(), apt2.toString());
    }
    
    @Test
    public void testShowError() {
        String errorMessage = "Test error message";

        view.showError(errorMessage);

        window.label("errorLabel").requireText(errorMessage);
    }
    
    @Test
    public void testAppointmentCreated() {
        TimeSlot slot = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        Appointment appointment = new Appointment("APT001", "John Doe", slot);

        view.appointmentCreated(appointment);

        String[] listContents = window.list("appointmentList").contents();
        assertThat(listContents).contains(appointment.toString());
        
        window.label("errorLabel").requireText(" ");
    }
    
    @Test
    public void testAppointmentDeleted() {
        TimeSlot slot = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        Appointment appointment = new Appointment("APT001", "John Doe", slot);
        
        view.appointmentCreated(appointment);
        String[] beforeDelete = window.list("appointmentList").contents();
        assertThat(beforeDelete).contains(appointment.toString());

        view.appointmentDeleted("APT001");

        String[] afterDelete = window.list("appointmentList").contents();
        assertThat(afterDelete).doesNotContain(appointment.toString());
        
        window.label("errorLabel").requireText(" ");
    }
    
    @Test
    public void testAppointmentDeletedMultipleAppointments() {
        TimeSlot slot = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        Appointment apt1 = new Appointment("APT001", "John Doe", slot);
        Appointment apt2 = new Appointment("APT002", "Jane Smith", slot);
        Appointment apt3 = new Appointment("APT003", "Bob Johnson", slot);
        
        view.appointmentCreated(apt1);
        view.appointmentCreated(apt2);
        view.appointmentCreated(apt3);
        
        String[] before = window.list("appointmentList").contents();
        assertThat(before).hasSize(3);
        
        view.appointmentDeleted("APT002");
        
        String[] after = window.list("appointmentList").contents();
        assertThat(after).hasSize(2);
        assertThat(after).doesNotContain(apt2.toString());
        assertThat(after).contains(apt1.toString(), apt3.toString());
    }
    
    @Test
    public void testRefreshButtonWhenControllerIsNull() {
        view.setHospitalController(null);
        
        window.button("refreshButton").click();
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testRefreshButtonShowsAllTimeSlots() {
        TimeSlot slot1 = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        TimeSlot slot2 = new TimeSlot("TS002", "Dr. Smith", "Neurology", "Room 202", LocalDateTime.now().plusDays(2));
        List<TimeSlot> expectedSlots = Arrays.asList(slot1, slot2);
        
        when(timeSlotRepository.findAll()).thenReturn(expectedSlots);

        window.button("refreshButton").click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String[] listContents = window.list("timeSlotList").contents();
        assertThat(listContents).containsExactly(slot1.toString(), slot2.toString());
        
        verify(timeSlotRepository, times(1)).findAll();
    }
    
    @Test
    public void testBookButtonWhenControllerIsNull() {
        view.setHospitalController(null);
        
        window.button("bookButton").click();
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testBookButtonCreatesAppointment() {
        TimeSlot slot = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        when(timeSlotRepository.findById("TS001")).thenReturn(slot);
        
        view.showAllTimeSlots(Arrays.asList(slot));
        
        window.textBox("patientNameTextBox").enterText("John Doe");
        
        window.list("timeSlotList").selectItem(0);

        window.button("bookButton").click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verify(timeSlotRepository, times(2)).findById("TS001");
        verify(appointmentRepository, times(1)).save(org.mockito.ArgumentMatchers.any(Appointment.class));
        
        String[] listContents = window.list("appointmentList").contents();
        assertThat(listContents).hasSize(1);
        assertThat(listContents[0]).contains("John Doe");
    }
    
    @Test
    public void testBookButtonShowsErrorWhenNoSlotSelected() {
        window.textBox("patientNameTextBox").enterText("John Doe");

        window.button("bookButton").click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        window.label("errorLabel").requireText("Please select a time slot");
        verify(appointmentRepository, never()).save(org.mockito.ArgumentMatchers.any(Appointment.class));
    }

    @Test
    public void testBookButtonShowsErrorWhenNoPatientName() {
        TimeSlot slot = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        view.showAllTimeSlots(Arrays.asList(slot));
        window.list("timeSlotList").selectItem(0);

        window.button("bookButton").click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        window.label("errorLabel").requireText("Please enter a patient name");
        verify(appointmentRepository, never()).save(org.mockito.ArgumentMatchers.any(Appointment.class));
    }
    
    @Test
    public void testDeleteButtonWhenControllerIsNull() {
        view.setHospitalController(null);
        
        window.button("deleteButton").click();
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testDeleteButtonDeletesAppointment() {
        TimeSlot slot = new TimeSlot("TS001", "Dr. House", "Cardiology", "Room 101", LocalDateTime.now().plusDays(1));
        Appointment appointment = new Appointment("APT001", "John Doe", slot);
        
        when(appointmentRepository.findById("APT001")).thenReturn(appointment);
        
        view.showAllAppointments(Arrays.asList(appointment));
        
        window.list("appointmentList").selectItem(0);

        window.button("deleteButton").click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verify(appointmentRepository, times(1)).delete("APT001");
        
        String[] listContents = window.list("appointmentList").contents();
        assertThat(listContents).isEmpty();
    }
    
    @Test
    public void testDeleteButtonShowsErrorWhenNoAppointmentSelected() {
        window.button("deleteButton").click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        window.label("errorLabel").requireText("Please select an appointment to delete");
        verify(appointmentRepository, never()).delete(org.mockito.ArgumentMatchers.anyString());
    }
    
    @Test
    public void testAddTimeSlotButtonWhenControllerIsNull() {
        view.setHospitalController(null);
        
        window.button("addTimeSlotButton").click();
        
        try { Thread.sleep(200); } catch (InterruptedException e) {}

        verify(timeSlotRepository, never()).save(org.mockito.ArgumentMatchers.any(TimeSlot.class));
    }
    
    @Test
    public void testAddTimeSlotButtonCallsController() {
        String doctorName = "Dr. New";
        String department = "Neurology";
        String roomNumber = "404";
        String dateTime = "2025-01-15 10:00";
        
        window.textBox("doctorNameTextBox").enterText(doctorName);
        window.textBox("departmentTextBox").enterText(department);
        window.textBox("roomNumberTextBox").enterText(roomNumber);
        window.textBox("dateTimeTextBox").enterText(dateTime);
        
        window.button("addTimeSlotButton").click();
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        verify(timeSlotRepository, times(1)).save(org.mockito.ArgumentMatchers.any(TimeSlot.class));
    }
    
    @Test
    public void testAddTimeSlotShowsErrorWhenDoctorMissing() {
        window.textBox("departmentTextBox").enterText("Cardiology");
        window.textBox("roomNumberTextBox").enterText("101");
        window.textBox("dateTimeTextBox").enterText("2025-01-15 10:00");
        
        window.button("addTimeSlotButton").click();
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        window.label("errorLabel").requireText("Please fill all fields");
        verify(timeSlotRepository, never()).save(org.mockito.ArgumentMatchers.any(TimeSlot.class));
    }
    
    @Test
    public void testAddTimeSlotShowsErrorWhenDepartmentMissing() {
        window.textBox("doctorNameTextBox").enterText("Dr. House");
        window.textBox("roomNumberTextBox").enterText("101");
        window.textBox("dateTimeTextBox").enterText("2025-01-15 10:00");
        
        window.button("addTimeSlotButton").click();
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        window.label("errorLabel").requireText("Please fill all fields");
        verify(timeSlotRepository, never()).save(org.mockito.ArgumentMatchers.any(TimeSlot.class));
    }
    
    @Test
    public void testAddTimeSlotShowsErrorWhenRoomNumMissing() {
        window.textBox("doctorNameTextBox").enterText("Dr. House");
        window.textBox("departmentTextBox").enterText("Cardiology");
        window.textBox("dateTimeTextBox").enterText("2025-01-15 10:00");
        
        window.button("addTimeSlotButton").click();
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        window.label("errorLabel").requireText("Please fill all fields");
        verify(timeSlotRepository, never()).save(org.mockito.ArgumentMatchers.any(TimeSlot.class));
    }
    
    @Test
    public void testAddTimeSlotShowsErrorWhenInvalidDateTime() {
        window.textBox("doctorNameTextBox").enterText("Dr. House");
        window.textBox("departmentTextBox").enterText("Cardiology");
        window.textBox("roomNumberTextBox").enterText("101");
        window.textBox("dateTimeTextBox").enterText("invalid-date");
        
        window.button("addTimeSlotButton").click();
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        String errorText = window.label("errorLabel").text();
        assertThat(errorText).contains("Invalid date");
        verify(timeSlotRepository, never()).save(org.mockito.ArgumentMatchers.any(TimeSlot.class));
    }
    
    @Test
    public void testAddTimeSlotButtonSuccess() {
        String doctorName = "Dr. New";
        String department = "Neurology";
        String roomNumber = "404";
        String dateTime = "2025-01-15 10:00";
        
        window.textBox("doctorNameTextBox").enterText(doctorName);
        window.textBox("departmentTextBox").enterText(department);
        window.textBox("roomNumberTextBox").enterText(roomNumber);
        window.textBox("dateTimeTextBox").enterText(dateTime);
        
        window.button("addTimeSlotButton").click();
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        verify(timeSlotRepository, times(1)).save(org.mockito.ArgumentMatchers.any(TimeSlot.class));
        
        assertThat(window.textBox("doctorNameTextBox").text()).isEmpty();
        assertThat(window.textBox("departmentTextBox").text()).isEmpty();
        assertThat(window.textBox("roomNumberTextBox").text()).isEmpty();
        assertThat(window.textBox("dateTimeTextBox").text()).isEmpty();
        
        window.label("errorLabel").requireText("Time slot added successfully!");
    }
}