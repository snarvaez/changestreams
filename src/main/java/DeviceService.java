import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.Date;

/**
 Incoming data from devices
 =======================
 DeviceEvents (V1)
 =======================
 {
 "_id" : ObjectId("5a261d8cbc54a05cb1e8a697"),
 "deviceId" : 81,
 "value" : 2410
 }

 =======================
 DeviceEvents (V2)
 =======================
 {
 "_id" : ObjectId("5a261e71bc54a05cb1ea8297"),
 "deviceId" : 171,
 "value" : 1684,
 "location" : {
     "type" : "Point",
     "coordinates" : [
         -87.33969,
         38.44392
     ]
   }
 }

 Analyze and store
 =======================
 Devices
 =======================
 {
 "_id" : 60,
 "value" : 546
 "date" : ISODate("2017-12-05T04:16:36.184Z"),
 "isFirstGen" : true,
 "isOutOfBounds" : true,
 "location" : {
     "type" : "Point",
     "coordinates" : [
         -87.33969,
         38.44392
     ]
  }
 }
 */

public class DeviceService {

  private MongoCollection<Document> coll;
  private MongoCollection<Document> collToWatch;

  DeviceService(MongoCollection<Document> collection, MongoCollection<Document> collectionToWatch) {

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

      Document device = changeStreamDocument.getFullDocument();

      System.out.println("OUT OF BOUNDS: " + device.toString());

      Bson filter = Filters.eq("_id", device.getInteger("deviceId")); //.getdeviceId());

      Bson update =  new Document("$set",
                                  new Document()
                                    .append("_id", device.getInteger("deviceId"))
                                    .append("value", device.getInteger("value"))
                                    .append("isOutOfBounds", true)
                                    .append("date", new Date()));

      if (device.containsKey("location"))
        ((Document)((Document) update).get("$set")).append("location", device.get("location"));

      UpdateOptions options = new UpdateOptions().upsert(true);

      coll.updateOne(filter, update, options);
    }
  };


  public Block<ChangeStreamDocument<Document>> firstGenHandler = new Block<ChangeStreamDocument<Document>>() {

    @Override
    public void apply(final ChangeStreamDocument<Document> changeStreamDocument) {

      Document device = changeStreamDocument.getFullDocument();

      System.out.println("FIRST GEN: " + device.toString());

      Bson filter = Filters.eq("_id", device.getInteger("deviceId"));

      Bson update =  new Document("$set",
              new Document()
                      .append("_id", device.getInteger("deviceId"))
                      .append("value", device.getInteger("value"))
                      .append("isFirstGen", true)
                      .append("date", new Date()));

      UpdateOptions options = new UpdateOptions().upsert(true);

      coll.updateOne(filter, update, options);
    }
  };
}