package repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import models.TimeSlot;
import repositories.mongo.TimeSlotMongoRepository;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class TimeSlotRepositoryTest {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;
    private TimeSlotRepository repository;
    private MongoCollection<org.bson.Document> timeSlotCollection;

    private static final String DB_NAME = "test-db";
    private static final String COLLECTION_NAME = "time-slot";

    @BeforeClass
    public static void setupServer() {
        server = new MongoServer(new MemoryBackend());
        serverAddress = server.bind();
    }

    @AfterClass
    public static void shutdownServer() {
        server.shutdown();
    }

    @Before
    public void setup() {
        client = new MongoClient(new ServerAddress(serverAddress));
        repository = new TimeSlotMongoRepository(client, DB_NAME, COLLECTION_NAME);  
        MongoDatabase database = client.getDatabase(DB_NAME);
        database.drop();
        timeSlotCollection = database.getCollection(COLLECTION_NAME);
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Test
    public void testFindAllWhenDatabaseIsEmpty() {
        assertThat(repository.findAll()).isEmpty();
    }
    
    @Test
    public void testFindAllWhenDatabaseHasRecords() {
        timeSlotCollection.insertOne(
            new org.bson.Document()
                .append("id", "1")
                .append("doctorName", "Dr. House")
                .append("department", "Cardiology")
                .append("roomNumber", "101")
                .append("appointmentDateTime", LocalDateTime.now().plusDays(1).toString())
        );
        
        timeSlotCollection.insertOne(
            new org.bson.Document()
                .append("id", "2")
                .append("doctorName", "Dr. Smith")
                .append("department", "Neurology")
                .append("roomNumber", "202")
                .append("appointmentDateTime", LocalDateTime.now().plusDays(2).toString())
        );
        
        List<TimeSlot> timeSlots = repository.findAll();
        assertThat(timeSlots).hasSize(2);
        assertThat(timeSlots.get(0).getId()).isEqualTo("1");
        assertThat(timeSlots.get(1).getId()).isEqualTo("2");
    }
    
    @Test
    public void testFindByIdWhenNotFound() {
        TimeSlot timeSlot = repository.findById("999");
        assertThat(timeSlot).isNull();
    }
    
    @Test
    public void testFindByIdWhenFound() {
        
        timeSlotCollection.insertOne(
            new org.bson.Document()
                .append("id", "1")
                .append("doctorName", "Dr. House")
                .append("department", "Cardiology")
                .append("roomNumber", "101")
                .append("appointmentDateTime", LocalDateTime.now().plusDays(1).toString())
        );
        
        TimeSlot timeSlot = repository.findById("1");
        assertThat(timeSlot).isNotNull();
        assertThat(timeSlot.getId()).isEqualTo("1");
        assertThat(timeSlot.getDoctorName()).isEqualTo("Dr. House");
        assertThat(timeSlot.getDepartment()).isEqualTo("Cardiology");
        assertThat(timeSlot.getRoomNumber()).isEqualTo("101");
        
    }
    
    @Test
    public void testSaveNewTimeSlot() {        
        TimeSlot timeSlot = new TimeSlot(
            "3", 
            "Dr. Wilson", 
            "Oncology", 
            "303", 
            LocalDateTime.now().plusDays(3)
        );
        
        repository.save(timeSlot);
        
        TimeSlot saved = repository.findById("3");
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo("3");
        assertThat(saved.getDoctorName()).isEqualTo("Dr. Wilson");
        assertThat(saved.getDepartment()).isEqualTo("Oncology");
        assertThat(saved.getRoomNumber()).isEqualTo("303");
    }
    
    @Test
    public void testDeleteTimeSlot() {        
        timeSlotCollection.insertOne(
            new org.bson.Document()
                .append("id", "1")
                .append("doctorName", "Dr. House")
                .append("department", "Cardiology")
                .append("roomNumber", "101")
                .append("appointmentDateTime", LocalDateTime.now().plusDays(1).toString())
        );
        
        assertThat(repository.findById("1")).isNotNull();
        
        repository.delete("1");
        
        assertThat(repository.findById("1")).isNull();
    }
}