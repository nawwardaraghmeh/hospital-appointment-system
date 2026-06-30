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
}