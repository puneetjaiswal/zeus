package org.zeus.presto;

import com.facebook.presto.spi.eventlistener.EventListener;
import com.facebook.presto.spi.eventlistener.QueryCompletedEvent;
import com.facebook.presto.spi.eventlistener.QueryCreatedEvent;
import com.facebook.presto.spi.eventlistener.SplitCompletedEvent;

import java.util.ArrayList;
import java.util.List;

public class QueryEventListener implements EventListener {
    private List<EventListener> eventListeners = new ArrayList<>();

    public QueryEventListener() {
        this.eventListeners.add(new QueryLoggerEventListener());
    }

    /**
     * This event is triggered when the query is created.
     */
    public void queryCreated(QueryCreatedEvent queryCreatedEvent) {
        for (EventListener e : eventListeners) {
            e.queryCreated(queryCreatedEvent);
        }
    }

    /**
     * This event is triggered when query is completed.
     */
    public void queryCompleted(QueryCompletedEvent queryCompletedEvent) {
        for (EventListener e : eventListeners) {
            e.queryCompleted(queryCompletedEvent);
        }
    }

    public void splitCompleted(SplitCompletedEvent splitCompletedEvent) {
        for (EventListener e : eventListeners) {
            e.splitCompleted(splitCompletedEvent);
        }
    }
}
