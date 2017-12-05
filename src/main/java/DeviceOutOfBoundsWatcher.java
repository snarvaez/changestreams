import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.Document;

import static java.util.Arrays.asList;

public class DeviceOutOfBoundsWatcher extends Thread {

  private Block<ChangeStreamDocument<Document>> handler;
  private MongoCollection<Document> coll;

  DeviceOutOfBoundsWatcher(MongoCollection<Document> coll, Block<ChangeStreamDocument<Document>> handler) {
    this.handler = handler;
    this.coll = coll;
  }

  @Override
  public void run() {

    coll.watch(asList(
            Aggregates.match(
                    Filters.in("operationType", asList("insert", "update", "replace"))),
            Aggregates.match(
                    Filters.or(asList(
                            Filters.lt("fullDocument.value", 1000),
                            Filters.gt("fullDocument.value", 2000))))))
            .fullDocument(FullDocument.UPDATE_LOOKUP)
            .forEach(handler);
  }
}
