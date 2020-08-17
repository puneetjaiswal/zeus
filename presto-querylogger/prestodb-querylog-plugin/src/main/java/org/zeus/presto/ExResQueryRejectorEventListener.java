package org.zeus.presto;

import io.airlift.log.Logger;
import com.facebook.presto.spi.PrestoException;
import com.facebook.presto.spi.StandardErrorCode;
import com.facebook.presto.spi.eventlistener.EventListener;
import com.facebook.presto.spi.eventlistener.QueryCompletedEvent;
import com.facebook.presto.spi.eventlistener.QueryCreatedEvent;

import java.util.HashSet;

public class ExResQueryRejectorEventListener implements EventListener {

    private static final Logger logger = Logger.get(ExResQueryRejectorEventListener.class);

    private static final HashSet<String> recentlyFailedQueries = new HashSet<>();

    // TODO: load these from external props file
    public static final int ROW_SCAN_LIMIT = 10_000_000; // 10M output row scan limit

    public void queryCreated(QueryCreatedEvent queryCreatedEvent) {
        String rawQueryText = queryCreatedEvent.getMetadata().getQuery();
        if (rawQueryText.contains(Constants.HINT_TO_DISABLE_QUERY_REJECTOR)) {
            return;
        }

        if (recentlyFailedQueries.contains(rawQueryText.toLowerCase())) {
            logger.error("Rejected query from event listener: " + rawQueryText);
            throw new PrestoException(StandardErrorCode.QUERY_REJECTED,
                    "This query has failed before, please don't run expensive queries.");
        }
    }

    public void queryCompleted(QueryCompletedEvent queryCompletedEvent) {
        if (recentlyFailedQueries.size() > 5000) {
            logger.warn("Resetting the bad query cache.");
            // TODO: replace recentlyFailedQueries with guava cache
            recentlyFailedQueries.clear();
        }

        if (queryCompletedEvent.getFailureInfo().isPresent()) {
            String queryText = queryCompletedEvent.getMetadata().getQuery();
            boolean queryMaxedMemoryOut = queryCompletedEvent.getFailureInfo().get().getFailuresJson().contains("Query exceeded max memory size");
            if (queryMaxedMemoryOut) {
                recentlyFailedQueries.add(queryText);
                return;
            }
            boolean queryTimedOut = queryCompletedEvent.getFailureInfo().get().getFailuresJson().contains("Query exceeded maximum time limit");
            if (queryTimedOut) {
                boolean queryScannedRows = queryCompletedEvent.getStatistics().getTotalRows() > ROW_SCAN_LIMIT;
                if (queryScannedRows) {
                    recentlyFailedQueries.add(queryText);
                }
            }
        }
    }
}
