import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

public class DeviceService {

  private MongoCollection<Device> coll;
  private MongoCollection<Document> collToWatch;

  DeviceService(MongoCollection<Device> collection, MongoCollection<Document> collectionToWatch) {

    this.coll = collection;
    this.collToWatch = collectionToWatch;

  }

  public void watchOutOfBounds() {

    DeviceOutOfBoundsWatcher outOfBoundsWatcher = new DeviceOutOfBoundsWatcher(collToWatch, OutOfBoundsHandler);

    outOfBoundsWatcher.start();

  }

  public void watchFirstGen() {

    DeviceFirstGenWatcher firstGenWatcher = new DeviceFirstGenWatcher(collToWatch, firstGenHandler);

    firstGenWatcher.start();

  }

  public Block<ChangeStreamDocument<Document>> OutOfBoundsHandler = new Block<ChangeStreamDocument<Document>>() {

    @Override
    public void apply(final ChangeStreamDocument<Document> changeStreamDocument) {

      Device device = new Device(changeStreamDocument.getFullDocument());

      System.out.println("DEVICE OUT OF BOUNDS: " + changeStreamDocument.getFullDocument().toString());

      Bson filter = Filters.eq("_id", device.getdeviceId());

      Bson update =  new Document("$set",
                                  new Document()
                                    .append("_id", device.getdeviceId())
                                    .append("value", device.getvalue())
                                    .append("date", device.getDate()));

      UpdateOptions options = new UpdateOptions().upsert(true);

      coll.updateOne(filter, update, options);

    }

  };


  public Block<ChangeStreamDocument<Document>> firstGenHandler = new Block<ChangeStreamDocument<Document>>() {

    @Override
    public void apply(final ChangeStreamDocument<Document> changeStreamDocument) {

      Device device = new Device(changeStreamDocument.getFullDocument());

      System.out.println("FIRST GEN DEVICE: " + device.getdeviceId());

      Bson filter = Filters.eq("_id", device.getdeviceId());

      Bson update =  new Document("$set",
              new Document()
                      .append("_id", device.getdeviceId())
                      .append("value", device.getvalue())
                      .append("firstGen", true) // FLAG 1ST GEN DEVICES
                      .append("date", device.getDate()));

      UpdateOptions options = new UpdateOptions().upsert(true);

      coll.updateOne(filter, update, options);

    }

  };

}