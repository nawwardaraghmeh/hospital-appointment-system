package views;

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
    private JTextField doctorNameTextField;
    private JTextField departmentTextField;
    private JTextField roomNumberTextField;
    private JTextField dateTimeTextField;
    private JButton addTimeSlotButton;

    public HospitalSwingView() {
        setTitle("Hospital Appointment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        initComponents();
        setupActionListeners();
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
        
        // add time slot section
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new JLabel("Add New Time Slot"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Doctor:"), gbc);

        doctorNameTextField = new JTextField(15);
        doctorNameTextField.setName("doctorNameTextBox");
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(doctorNameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Department:"), gbc);

        departmentTextField = new JTextField(15);
        departmentTextField.setName("departmentTextBox");
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(departmentTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Room:"), gbc);

        roomNumberTextField = new JTextField(10);
        roomNumberTextField.setName("roomNumberTextBox");
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(roomNumberTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Date/Time (YYYY-MM-DD HH:MM):"), gbc);

        dateTimeTextField = new JTextField(20);
        dateTimeTextField.setName("dateTimeTextBox");
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(dateTimeTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.CENTER;
        addTimeSlotButton = new JButton("Add Time Slot");
        addTimeSlotButton.setName("addTimeSlotButton");
        mainPanel.add(addTimeSlotButton, gbc);
        
        add(mainPanel);
    }

    public void setHospitalController(HospitalController controller) {
        this.controller = controller;
    }
    
    private void setupActionListeners() {
        refreshButton.addActionListener(e -> {
            if (controller != null) {
                controller.getAllTimeSlots();
            }
        });
        
        bookButton.addActionListener(e -> {
            if (controller != null) {
                String selectedSlot = timeSlotList.getSelectedValue();
                String patientName = patientNameTextField.getText();
                
                if (selectedSlot == null) {
                    showError("Please select a time slot");
                    return;
                }
                
                if (patientName.trim().isEmpty()) {
                    showError("Please enter a patient name");
                    return;
                }
                
                String id = selectedSlot.substring(
                    selectedSlot.indexOf("'") + 1,
                    selectedSlot.indexOf("'", selectedSlot.indexOf("'") + 1)
                );
                
                TimeSlot timeSlot = controller.getTimeSlotById(id);
                Appointment appointment = new Appointment(
                    "APT" + System.currentTimeMillis(), 
                    patientName, 
                    timeSlot
                );
                controller.createAppointment(appointment);
                patientNameTextField.setText("");
            }
        });
        
        deleteButton.addActionListener(e -> {
            if (controller != null) {
                String selectedAppointment = appointmentList.getSelectedValue();
                
                if (selectedAppointment == null) {
                    showError("Please select an appointment to delete");
                    return;
                }
                
                String id = selectedAppointment.substring(
                    selectedAppointment.indexOf("'") + 1,
                    selectedAppointment.indexOf("'", selectedAppointment.indexOf("'") + 1)
                );
                controller.deleteAppointment(id);
            }
        });
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
	    	SwingUtilities.invokeLater(() -> {
	        appointmentListModel.addElement(appointment.toString());
	        errorLabel.setText(" ");
	    	});
    }

    @Override
    public void appointmentDeleted(String appointmentId) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < appointmentListModel.size(); i++) {
                String item = appointmentListModel.getElementAt(i);
                if (item.contains(appointmentId)) {
                    appointmentListModel.remove(i);
                    break;
                }
            }
            errorLabel.setText(" ");
        });
    }
   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HospitalSwingView().setVisible(true);
        });
    }
}