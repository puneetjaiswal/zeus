package org.zeus.presto;

import com.facebook.presto.spi.eventlistener.EventListener;
import com.facebook.presto.spi.eventlistener.EventListenerFactory;

import java.util.Map;

public class QueryLogEventListenerFactory implements EventListenerFactory {
  public String getName() {
    return "querylog-event-listener";
  }

  public EventListener create(Map<String, String> config) {
    return new QueryEventListener();
  }
}
