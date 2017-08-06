package org.skywalking.apm.mock.collector.entity;

import java.util.ArrayList;
import java.util.List;
import org.skywalking.apm.network.proto.TraceSegmentReference;
import org.skywalking.apm.network.proto.UniqueId;

public class Segment {
    private String segmentId;
    private List<Span> spans;
    private List<SegmentRef> refs;

    public Segment(UniqueId segmentId) {
        this.segmentId = segmentId.getIdParts(0) + "" + segmentId.getIdParts(1) + "" + segmentId.getIdParts(2);
        spans = new ArrayList<>();
        refs = new ArrayList<>();
    }

    public static class SegmentRef {
        private int parentServiceId;
        private String parentServiceName;
        private int networkAddressId;
        private int entryServiceId;
        private String refType;
        private int parentSpanId;
        private String parentTraceSegmentId;
        private int parentApplicationInstanceId;
        private String networkAddress;
        private String entryServiceName;

        public SegmentRef(TraceSegmentReference ref) {
            UniqueId uniqueId = ref.getParentTraceSegmentId();
            this.parentTraceSegmentId = uniqueId.getIdParts(0) + "" + uniqueId.getIdParts(1) + "" + uniqueId.getIdParts(2);
            this.refType = ref.getRefType().toString();
            this.parentSpanId = ref.getParentSpanId();
            this.entryServiceId = ref.getEntryServiceId();
            this.networkAddressId = ref.getNetworkAddressId();
            this.parentApplicationInstanceId = ref.getParentApplicationInstanceId();
            this.parentServiceId = ref.getParentServiceId();
            this.parentServiceName = ref.getParentServiceName();
            this.networkAddress = ref.getNetworkAddress();
            this.entryServiceName = ref.getEntryServiceName();
        }

        public int getParentSpanId() {
            return parentSpanId;
        }

        public String getParentTraceSegmentId() {
            return parentTraceSegmentId;
        }

        public String getNetworkAddress() {
            return networkAddress;
        }

        public String getEntryServiceName() {
            return entryServiceName;
        }

        public int getParentServiceId() {
            return parentServiceId;
        }

        public String getParentServiceName() {
            return parentServiceName;
        }

        public int getNetworkAddressId() {
            return networkAddressId;
        }

        public int getEntryServiceId() {
            return entryServiceId;
        }

        public String getRefType() {
            return refType;
        }

        public int getParentApplicationInstanceId() {
            return parentApplicationInstanceId;
        }
    }

    public Segment addSpan(Span span) {
        spans.add(span);
        return this;
    }

    public Segment addRefs(SegmentRef segmentRef) {
        refs.add(segmentRef);
        return this;
    }

    public static class Span {
        private String operationName;
        private int operationId;
        private int parentSpanId;
        private int spanId;
        private String spanLayer;
        private List<KeyValuePair> tags;
        private List<LogEvent> logs;
        private long startTime;
        private long endTime;
        private int componentId;
        private String componentName;
        private boolean isError;
        private String spanType;
        private String peer;
        private int peerId;

        public Span(String operationName) {
            this.operationName = operationName;
            tags = new ArrayList<>();
            logs = new ArrayList<>();
        }
    }

    public static class LogEvent {
        private List<KeyValuePair> logEvent;

        public LogEvent() {
            this.logEvent = new ArrayList<>();
        }
    }

    public static class KeyValuePair {
        private String key;
        private String value;

        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }
    }

    public static class LogEventBuilder {
        private LogEvent logEvent;

        private LogEventBuilder() {
            logEvent = new LogEvent();
        }

        public static LogEventBuilder newBuilder() {
            return new LogEventBuilder();
        }

        public LogEventBuilder addLog(String key, String value) {
            logEvent.logEvent.add(new KeyValuePair(key, value));
            return this;
        }

        public LogEvent build() {
            return logEvent;
        }
    }

    public static class SpanBuilder {
        private Span span;

        private SpanBuilder(String operationName) {
            this.span = new Span(operationName);
        }

        public static SpanBuilder newBuilder(String operationName) {
            return new SpanBuilder(operationName);
        }

        public SpanBuilder parentSpanId(int parentSpanId) {
            span.parentSpanId = parentSpanId;
            return this;
        }

        public SpanBuilder spanId(int spanId) {
            span.spanId = spanId;
            return this;
        }

        public SpanBuilder spanLayer(String spanLayer) {
            span.spanLayer = spanLayer;
            return this;
        }

        public SpanBuilder tags(String key, String value) {
            span.tags.add(new KeyValuePair(key, value));
            return this;
        }

        public SpanBuilder startTime(long startTime) {
            span.startTime = startTime;
            return this;
        }

        public SpanBuilder endTime(long endTime) {
            span.endTime = endTime;
            return this;
        }

        public SpanBuilder componentName(String componentName) {
            span.componentName = componentName;
            return this;
        }

        public SpanBuilder componentId(int componentId) {
            span.componentId = componentId;
            return this;
        }

        public SpanBuilder logs(LogEvent logEvent) {
            span.logs.add(logEvent);
            return this;
        }

        public Span build() {
            return span;
        }

        public SpanBuilder spanType(String spanType) {
            span.spanType = spanType;
            return this;
        }

        public SpanBuilder peerId(int peerId) {
            span.peerId = peerId;
            return this;
        }

        public SpanBuilder peer(String peer) {
            span.peer = peer;
            return this;
        }

        public SpanBuilder operationId(int operationId) {
            span.operationId = operationId;
            return this;
        }
    }

    public static class SegmentRefBuilder {
        private SegmentRef segmentRef;

        private SegmentRefBuilder(TraceSegmentReference reference) {
            segmentRef = new SegmentRef(reference);
        }

        public static SegmentRefBuilder newBuilder(TraceSegmentReference reference) {
            return new SegmentRefBuilder(reference);
        }

        public SegmentRef build() {
            return segmentRef;
        }
    }

    public static class SegmentBuilder {
        private Segment segment;

        private SegmentBuilder(UniqueId segmentId) {
            segment = new Segment(segmentId);
        }

        public static SegmentBuilder newBuilder(UniqueId segmentId) {

            return new SegmentBuilder(segmentId);
        }

        public SegmentBuilder addSpan(Span span) {
            segment.spans.add(span);
            return this;
        }

        public SegmentBuilder addRefs(SegmentRef segmentRef) {
            segment.refs.add(segmentRef);
            return this;
        }

        public Segment build() {
            return segment;
        }
    }

}
