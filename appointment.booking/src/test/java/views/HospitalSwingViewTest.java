package views;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
}