package repositories.mongo;

import models.TimeSlot;
import repositories.TimeSlotRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class TimeSlotMongoRepository implements TimeSlotRepository {

    private final MongoCollection<Document> timeSlotCollection;

    public TimeSlotMongoRepository(com.mongodb.MongoClient client, 
                                   String databaseName, String collectionName) {
        this.timeSlotCollection = client.getDatabase(databaseName)
                                        .getCollection(collectionName);
    }

    @Override
    public List<TimeSlot> findAll() {
        return StreamSupport.stream(timeSlotCollection.find().spliterator(), false)
                .map(this::fromDocumentToTimeSlot)
                .collect(Collectors.toList());
    }
    
    @Override
    public TimeSlot findById(String id) { 
        Document doc = timeSlotCollection.find(Filters.eq("id", id)).first();
        if (doc != null) {
            return fromDocumentToTimeSlot(doc);
        }
        return null;
    }

    private TimeSlot fromDocumentToTimeSlot(Document doc) {
        return new TimeSlot(
                doc.getString("id"),
                doc.getString("doctorName"),
                doc.getString("department"),
                doc.getString("roomNumber"),
                LocalDateTime.parse(doc.getString("appointmentDateTime"))
        );
    }
}