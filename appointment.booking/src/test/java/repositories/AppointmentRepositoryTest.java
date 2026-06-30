package repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import models.Appointment;
import repositories.mongo.AppointmentMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class AppointmentRepositoryTest {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;
    private AppointmentRepository repository;
    private MongoCollection<org.bson.Document> appointmentCollection;

    private static final String DB_NAME = "test-db";
    private static final String COLLECTION_NAME = "appointment";

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
        repository = new AppointmentMongoRepository(client, DB_NAME, COLLECTION_NAME);
        MongoDatabase database = client.getDatabase(DB_NAME);
        database.drop();
        appointmentCollection = database.getCollection(COLLECTION_NAME);
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Test
    public void testFindAllWhenDatabaseIsEmpty() {
        List<Appointment> appointments = repository.findAll();
        assertThat(appointments).isEmpty();
    }
    
    @Test
    public void testFindAllWhenDatabaseHasRecords() {
        appointmentCollection.insertOne(
            new org.bson.Document()
                .append("id", "1")
                .append("patientName", "John Doe")
                .append("timeSlotId", "TS001")
        );
        
        appointmentCollection.insertOne(
            new org.bson.Document()
                .append("id", "2")
                .append("patientName", "Jane Smith")
                .append("timeSlotId", "TS002")
        );
        
        List<Appointment> appointments = repository.findAll();
        assertThat(appointments).hasSize(2);
        assertThat(appointments.get(0).getPatientName()).isEqualTo("John Doe");
        assertThat(appointments.get(1).getPatientName()).isEqualTo("Jane Smith");
    }
    
    @Test
    public void testFindByIdWhenNotFound() {
        Appointment appointment = repository.findById("999");
        assertThat(appointment).isNull();
    }

    @Test
    public void testFindByIdWhenFound() {
        appointmentCollection.insertOne(
            new org.bson.Document()
                .append("id", "1")
                .append("patientName", "John Doe")
                .append("timeSlotId", "TS001")
        );
        
        Appointment appointment = repository.findById("1");
        assertThat(appointment).isNotNull();
        assertThat(appointment.getId()).isEqualTo("1");
        assertThat(appointment.getPatientName()).isEqualTo("John Doe");
        assertThat(appointment.getTimeSlot().getId()).isEqualTo("TS001");
    }
}