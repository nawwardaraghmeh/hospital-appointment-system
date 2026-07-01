package views;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
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
    private DefaultListModel<String> timeSlotListModel;
    private JList<String> appointmentList;
    private DefaultListModel<String> appointmentListModel;  
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
    		JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        
        // time slot
        timeSlotListModel = new DefaultListModel<>();
        timeSlotList = new JList<>(timeSlotListModel);
        timeSlotList.setName("timeSlotList"); 
        JScrollPane timeSlotScrollPane = new JScrollPane(timeSlotList);
        timeSlotScrollPane.setName("timeSlotScrollPane");
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        mainPanel.add(new JLabel("Available Time Slots"), gbc);
        
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        mainPanel.add(timeSlotScrollPane, gbc);
        
        // appointment
        appointmentListModel = new DefaultListModel<>();
        appointmentList = new JList<>(appointmentListModel);
        appointmentList.setName("appointmentList");
        JScrollPane appointmentScrollPane = new JScrollPane(appointmentList);
        appointmentScrollPane.setName("appointmentScrollPane");

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        mainPanel.add(new JLabel("Appointments"), gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        mainPanel.add(appointmentScrollPane, gbc);
        
        // patient name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new JLabel("Patient Name:"), gbc);

        patientNameTextField = new JTextField(20);
        patientNameTextField.setName("patientNameTextBox");
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(patientNameTextField, gbc);
        
        // buttons 
        JPanel buttonPanel = new JPanel();

        bookButton = new JButton("Book Appointment");
        bookButton.setName("bookButton");
        refreshButton = new JButton("Refresh");
        refreshButton.setName("refreshButton");
        deleteButton = new JButton("Delete Appointment");
        deleteButton.setName("deleteButton");

        buttonPanel.add(bookButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);
        
        // error 
        errorLabel = new JLabel(" ");
        errorLabel.setName("errorLabel");
        errorLabel.setForeground(java.awt.Color.RED);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(errorLabel, gbc);
        
        add(mainPanel);
    }

    public void setHospitalController(HospitalController controller) {
        this.controller = controller;
    }

    @Override
    public void showAllTimeSlots(List<TimeSlot> timeSlots) {
    		SwingUtilities.invokeLater(() -> {
            timeSlotListModel.clear();
            for (TimeSlot slot : timeSlots) {
                timeSlotListModel.addElement(slot.toString());
            }
        });
    }

    @Override
    public void showAllAppointments(List<Appointment> appointments) {
    		SwingUtilities.invokeLater(() -> {
	        appointmentListModel.clear();
	        for (Appointment apt : appointments) {
	            appointmentListModel.addElement(apt.toString());
	        }
    		});
    }

    @Override
    public void showAvailableTimeSlots(List<TimeSlot> timeSlots) {
    		showAllTimeSlots(timeSlots);
    }

    @Override
    public void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            errorLabel.setText(message);
        });
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