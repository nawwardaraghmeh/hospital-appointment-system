package repositories.mongo;

import models.Appointment;
import repositories.AppointmentRepository;

import java.util.Collections;
import java.util.List;

import com.mongodb.client.MongoCollection;

import org.bson.Document;


public class AppointmentMongoRepository implements AppointmentRepository  {
    private final MongoCollection<Document> appointmentCollection;

    public AppointmentMongoRepository(com.mongodb.MongoClient client, 
                                      String databaseName, String collectionName) {
        this.appointmentCollection = client.getDatabase(databaseName)
                                           .getCollection(collectionName);
    }
    
    @Override
    public List<Appointment> findAll() {
        return Collections.emptyList();
    }
}
