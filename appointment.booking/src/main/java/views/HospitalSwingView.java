package views;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import controllers.HospitalController;
import models.Appointment;
import models.TimeSlot;
import views.HospitalView;

import java.util.List;

public class HospitalSwingView extends JFrame implements HospitalView {

    private static final long serialVersionUID = 1L;
    
    private HospitalController controller;
    
    private JList<String> timeSlotList;
    private JList<String> appointmentList;
    private JTextField patientNameTextField;
    private JButton bookButton;
    private JButton refreshButton;
    private JButton deleteButton;
    private JLabel errorLabel;

    public HospitalSwingView() {
        setTitle("Hospital Appointment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
    }

    public void setHospitalController(HospitalController controller) {
        this.controller = controller;
    }

    @Override
    public void showAllTimeSlots(List<TimeSlot> timeSlots) {
    }

    @Override
    public void showAllAppointments(List<Appointment> appointments) {
    }

    @Override
    public void showAvailableTimeSlots(List<TimeSlot> timeSlots) {
    }

    @Override
    public void showError(String message) {
    }

    @Override
    public void appointmentCreated(Appointment appointment) {
    }

    @Override
    public void appointmentDeleted(String appointmentId) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HospitalSwingView().setVisible(true);
        });
    }
}