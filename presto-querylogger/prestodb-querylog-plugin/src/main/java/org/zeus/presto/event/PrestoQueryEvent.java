package org.zeus.presto.event;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class PrestoQueryEvent {
    private String id;
    private long occurredAt;

    private String eventType;
    private long createdAt;
    private long executionStartedAt;
    private long endedAt;
    private long wallTimeSecs;

    // Failure related
    private String failureInfo;
    private int errorCode;
    private String errorCodeText;
    private String errorType;
    private String failureHost;
    private String failureTask;
    private String failureType;

    // Query stats
    private long analysisTimeSecs;
    private int completedSplits;
    private long cpuTimeSecs;
    private long queuedTimeSecs;
    private long totalInputBytes;
    private long totalInputRows;
    private long peakUserMemoryBytes;
    private long peakTaskTotalMemory;
    private long peakTotalNonRecoverableMemBytes;

    // Query context
    private String clustername;
    private String environment;
    private String version;
    private String user;
    private String source;
    private String serverAddress;

    // Query metadata
    private String queryText;
    private String queryId;
    private String state;
}
