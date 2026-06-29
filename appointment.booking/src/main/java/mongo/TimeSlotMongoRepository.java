package mongo;

import models.TimeSlot;
import repositories.TimeSlotRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import com.mongodb.client.MongoCollection;

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