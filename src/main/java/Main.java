import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {

  @SuppressWarnings({ "resource" })

  public static void main(String[] args) {

    // Server connection
    String mongoUri = "mongodb://127.0.0.1:27017,127.0.0.1:27018,127.0.0.1:27019";

    MongoClient client = new MongoClient(new MongoClientURI(mongoUri));

    MongoDatabase db = client.getDatabase("IoT");

    // Drop DeviceEvents collection and set to capped with max
    db.getCollection("DeviceEvents").drop();

    // Capped coll 1GB || 5,000 docs
    db.createCollection("DeviceEvents", new CreateCollectionOptions()
            .capped(true)
            .maxDocuments(5000)
            .sizeInBytes(1073741824));

    // Collections
    MongoCollection<Document> collDeviceEvents = db.getCollection("DeviceEvents");
    MongoCollection<Document> collDevices = db.getCollection("Devices");

    // Get Service and Watch!
    DeviceService deviceSvc = new DeviceService(collDevices, collDeviceEvents);

    deviceSvc.watchOutOfBounds();

    deviceSvc.watchFirstGen();

  }
}