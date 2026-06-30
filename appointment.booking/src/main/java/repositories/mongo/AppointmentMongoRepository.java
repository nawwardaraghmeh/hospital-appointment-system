package repositories.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

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
