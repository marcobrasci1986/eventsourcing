package be.avidoo;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.RecordedEvent;
import com.eventstore.dbclient.ResolvedEvent;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class SampleRead {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        EventStoreDBClientSettings settings = EventStoreDBConnectionString.
                parseOrThrow("esdb://localhost:2113?tls=false");

        EventStoreDBClient client = EventStoreDBClient.create(settings);

        ReadStreamOptions options =
                ReadStreamOptions.get()  // Create a read option for client to read events
                        .forwards()      // Client should read events forward in time
                        .fromStart()     // Client should read from the start of stream
                        .maxCount(10);   // Client should read at most 10 events

        String eventStream = "SampleStream";
        ReadResult result = client.readStream(eventStream, options).get();

        for (ResolvedEvent resolvedEvent : result.getEvents()) {                                 // For each event in stream
            RecordedEvent recordedEvent = resolvedEvent.getOriginalEvent();                  // Get the original event (can ignore for now)
            //
            System.out.println("************************");                                  //
            System.out.println("You have read an event!");                                   //
            System.out.println("Stream: " + recordedEvent.getStreamId());                    // Print the stream name of the event
            System.out.println("Event Type: " + recordedEvent.getEventType());               // Print the type of the event
            System.out.println("Event Body: " + new String(recordedEvent.getEventData(),     // Print the body of the event after converting it from a byte array
                    StandardCharsets.UTF_8));         // UTF8 is used to convert byte array to string

        }
    }
}