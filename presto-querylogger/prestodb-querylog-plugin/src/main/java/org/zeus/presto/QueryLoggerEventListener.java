package org.zeus.presto;

import io.airlift.log.Logger;
import com.facebook.presto.spi.eventlistener.EventListener;
import com.facebook.presto.spi.eventlistener.QueryCompletedEvent;
import com.facebook.presto.spi.eventlistener.QueryContext;
import com.facebook.presto.spi.eventlistener.QueryCreatedEvent;
import com.facebook.presto.spi.eventlistener.QueryFailureInfo;
import com.facebook.presto.spi.eventlistener.QueryMetadata;
import com.facebook.presto.spi.eventlistener.QueryStatistics;
import org.zeus.presto.event.PrestoQueryEvent;

import java.time.Duration;
import java.util.UUID;

public class QueryLoggerEventListener implements EventListener {
    private static final Logger logger = Logger.get(QueryLoggerEventListener.class);

    // TODO: load this from props...
    private String clusterName;
    private String environment;
    private String version;

    public QueryLoggerEventListener() {
        // TODO: load this from props...
        clusterName = "xyz";
        environment = "Prod-1";
        version = "0.239";
    }

    public void queryCreated(QueryCreatedEvent queryCreatedEvent) {
        PrestoQueryEvent.PrestoQueryEventBuilder event = queryCreateEvent(queryCreatedEvent);
        pushEvent(event);
    }

    public void queryCompleted(QueryCompletedEvent queryCompletedEvent) {
        PrestoQueryEvent.PrestoQueryEventBuilder event = queryCompleteEvent(queryCompletedEvent);
        pushEvent(event);
    }

    private void pushEvent(PrestoQueryEvent.PrestoQueryEventBuilder eventBuilder) {
        writeEventMetadataFields(eventBuilder);
        try {
            // TODO: Sink this event to a store
            logger.info("Logging query event: ", eventBuilder.build().toString());
        } catch (Exception e) {
            logger.warn(e, "Error in pushing presto event to the log store!");
        }
    }

    private void writeEventMetadataFields(PrestoQueryEvent.PrestoQueryEventBuilder event) {
        event.id(UUID.randomUUID().toString());
        event.occurredAt(System.currentTimeMillis());
    }

    public PrestoQueryEvent.PrestoQueryEventBuilder queryCompleteEvent(QueryCompletedEvent queryCompletedEvent) {
        PrestoQueryEvent.PrestoQueryEventBuilder event = PrestoQueryEvent.builder();
        event.eventType(Constants.QUERY_COMPLETED_EVENT);
        event.endedAt(queryCompletedEvent.getEndTime().toEpochMilli());
        event.executionStartedAt(queryCompletedEvent.getExecutionStartTime().toEpochMilli());

        event.createdAt(queryCompletedEvent.getCreateTime().toEpochMilli());
        Duration wallTime = Duration
                .between(queryCompletedEvent.getCreateTime(), queryCompletedEvent.getEndTime());
        event.wallTimeSecs(wallTime.getSeconds());

        if (queryCompletedEvent.getFailureInfo().isPresent()) {
            QueryFailureInfo queryFailureInfo = queryCompletedEvent.getFailureInfo().get();

            event.failureInfo(queryFailureInfo.getFailuresJson());
            event.errorCode(queryFailureInfo.getErrorCode().getCode());
            event.errorCodeText(queryFailureInfo.getErrorCode().getName());
            event.errorType(queryFailureInfo.getErrorCode().getType().name());

            if (queryFailureInfo.getFailureHost().isPresent()) {
                event.failureHost(queryFailureInfo.getFailureHost().get());
            }
            if (queryFailureInfo.getFailureTask().isPresent()) {
                event.failureTask(queryFailureInfo.getFailureTask().get());
            }
            if (queryFailureInfo.getFailureType().isPresent()) {
                event.failureType(queryFailureInfo.getFailureType().get());
            }
        }

        writeQueryStats(event, queryCompletedEvent.getStatistics());
        writeQueryMetadata(event, queryCompletedEvent.getMetadata());
        writeQueryContext(event, queryCompletedEvent.getContext());
        return event;
    }

    public PrestoQueryEvent.PrestoQueryEventBuilder queryCreateEvent(QueryCreatedEvent queryCreatedEvent) {
        PrestoQueryEvent.PrestoQueryEventBuilder event = PrestoQueryEvent.builder();
        event.eventType(Constants.QUERY_CREATED_EVENT);
        event.createdAt(queryCreatedEvent.getCreateTime().toEpochMilli());
        writeQueryMetadata(event, queryCreatedEvent.getMetadata());
        writeQueryContext(event, queryCreatedEvent.getContext());
        return event;
    }

    private void writeQueryStats(PrestoQueryEvent.PrestoQueryEventBuilder event, QueryStatistics statistics) {
        if (statistics.getAnalysisTime().isPresent()) {
            event.analysisTimeSecs(statistics.getAnalysisTime().get().getSeconds());
        }
        event.completedSplits(statistics.getCompletedSplits());
        event.cpuTimeSecs(statistics.getCpuTime().getSeconds());
        event.queuedTimeSecs(statistics.getQueuedTime().getSeconds());
        event.totalInputBytes(statistics.getTotalBytes());
        event.totalInputRows(statistics.getTotalRows());
        event.peakUserMemoryBytes(statistics.getPeakUserMemoryBytes());
        event.peakTaskTotalMemory(statistics.getPeakTaskTotalMemory());
        event.peakTotalNonRecoverableMemBytes(statistics.getPeakTotalNonRevocableMemoryBytes());
    }

    private void writeQueryContext(PrestoQueryEvent.PrestoQueryEventBuilder event, QueryContext context) {
        event.environment(environment);
        event.clustername(clusterName);
        event.version(version);
        if (context.getSource().isPresent()) {
            event.source(context.getSource().get());
        }
        event.serverAddress(context.getServerAddress());
        event.user(context.getUser());
    }

    private void writeQueryMetadata(PrestoQueryEvent.PrestoQueryEventBuilder event, QueryMetadata metadata) {
        String queryText = metadata.getQuery();
        event.queryText(queryText);
        event.queryId(metadata.getQueryId());
        event.state(metadata.getQueryState());
    }
}
