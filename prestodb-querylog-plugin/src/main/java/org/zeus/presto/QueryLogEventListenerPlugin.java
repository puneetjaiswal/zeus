package org.zeus.presto;

import com.facebook.presto.spi.Plugin;
import com.facebook.presto.spi.eventlistener.EventListenerFactory;

import java.util.ArrayList;
import java.util.List;

public class QueryLogEventListenerPlugin implements Plugin {
  public Iterable<EventListenerFactory> getEventListenerFactories() {
    List<EventListenerFactory> list = new ArrayList<>();
    list.add(new QueryLogEventListenerFactory());
    return list;
  }
}
