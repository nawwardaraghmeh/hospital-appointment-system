package repositories.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import models.Appointment;
import models.TimeSlot;
import repositories.AppointmentRepository;


public class AppointmentMongoRepository implements AppointmentRepository  {
    private final MongoCollection<Document> appointmentCollection;

    public AppointmentMongoRepository(com.mongodb.MongoClient client, 
                                      String databaseName, String collectionName) {
        this.appointmentCollection = client.getDatabase(databaseName)
                                           .getCollection(collectionName);
    }
    
    @Override
    public List<Appointment> findAll() {
        return StreamSupport.stream(appointmentCollection.find().spliterator(), false)
                .map(this::fromDocumentToAppointment)
                .collect(Collectors.toList());
    }
    
    @Override
    public Appointment findById(String id) {
        Document doc = appointmentCollection.find(Filters.eq("id", id)).first();
        if (doc != null) {
            return fromDocumentToAppointment(doc);
        }
        return null;
    }
    
    @Override
    public void save(Appointment appointment) {
        Document existing = appointmentCollection.find(Filters.eq("id", appointment.getId())).first();
        
        if (existing != null) {
            appointmentCollection.updateOne(
                Filters.eq("id", appointment.getId()),
                Updates.combine(
                    Updates.set("patientName", appointment.getPatientName()),
                    Updates.set("timeSlotId", appointment.getTimeSlot().getId())
                )
            );
        } else {
            appointmentCollection.insertOne(
                new Document()
                    .append("id", appointment.getId())
                    .append("patientName", appointment.getPatientName())
                    .append("timeSlotId", appointment.getTimeSlot().getId())
            );
        }
    }
    
    @Override
    public void delete(String id) {
        appointmentCollection.deleteOne(Filters.eq("id", id));
    }
    
    private Appointment fromDocumentToAppointment(Document doc) {
        String timeSlotId = doc.getString("timeSlotId");
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(timeSlotId);
        
        return new Appointment(
                doc.getString("id"),
                doc.getString("patientName"),
                timeSlot
        );
    }
}
