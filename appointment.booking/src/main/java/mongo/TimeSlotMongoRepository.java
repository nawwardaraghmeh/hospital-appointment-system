package mongo;


import models.TimeSlot;
import repositories.TimeSlotRepository;

import java.util.Collections;
import java.util.List;

public class TimeSlotMongoRepository implements TimeSlotRepository {

    public TimeSlotMongoRepository(com.mongodb.MongoClient client, 
                                   String databaseName, String collectionName) {
    }

    @Override
    public List<TimeSlot> findAll() {
        return Collections.emptyList();
    }
}