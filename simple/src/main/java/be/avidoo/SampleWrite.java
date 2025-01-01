package be.avidoo;

import com.eventstore.dbclient.AppendToStreamOptions;
import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.ExpectedRevision;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class SampleWrite {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        EventStoreDBClientSettings settings = EventStoreDBConnectionString.
                parseOrThrow("esdb://localhost:2113?tls=false");

        EventStoreDBClient client = EventStoreDBClient.create(settings);

        String eventType = "SampleEventType";                                  // Define the name of the event type for the new event
        byte[] eventBody = "{\"id\":\"1\", \"importantData\":\"some value\"}"  // Define the body of the event in a UTF8 encoded byte array
                .getBytes(StandardCharsets.UTF_8);

        EventData eventData = EventData.builderAsJson(eventType, eventBody)                  // Create the new event object with the type and body
                .build();

        AppendToStreamOptions options = AppendToStreamOptions.get().expectedRevision(ExpectedRevision.any());

        String eventStream = "SampleStream";
        client.appendToStream(eventStream, options, eventData).get();

        System.out.println("************************");
        System.out.println("🎉 Congratulations, you have written an event!");
        System.out.println("Stream: " + eventStream);
        System.out.println("Event Type: " + eventType);
        System.out.println("Event Body: {\"id\":\"1\",\"importantData\":\"some value\"}");
        System.out.println("************************");
    }
}
