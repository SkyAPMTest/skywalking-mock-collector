package org.skywalking.apm.mock.collector.entity;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skywalking.apm.network.proto.KeyWithStringValue;
import org.skywalking.apm.network.proto.LogMessage;
import org.skywalking.apm.network.proto.SpanObject;
import org.skywalking.apm.network.proto.TraceSegmentObject;
import org.skywalking.apm.network.proto.TraceSegmentReference;

public class SegmentItems {
    private Logger logger = LogManager.getLogger(SegmentItem.class);

    private AtomicLong segmentCount = new AtomicLong();
    private Map<String, SegmentItem> segmentItems;

    public SegmentItems() {
        this.segmentItems = new HashMap<>();
    }

    public SegmentItems addSegmentItem(ByteString segment) {
        long currentSegmentCount = segmentCount.incrementAndGet();

        if (currentSegmentCount % 100000 == 0) {
            try {
                TraceSegmentObject traceSegmentObject = TraceSegmentObject.parseFrom(segment);
                Segment.SegmentBuilder segmentBuilder = Segment.SegmentBuilder.newBuilder(traceSegmentObject.getTraceSegmentId(), traceSegmentObject.getApplicationId(), traceSegmentObject.getApplicationInstanceId());

                for (SpanObject spanObject : traceSegmentObject.getSpansList()) {
                    Segment.SpanBuilder spanBuilder = Segment.SpanBuilder.newBuilder(spanObject.getOperationName()).parentSpanId(spanObject.getParentSpanId())
                        .spanId(spanObject.getSpanId()).componentId(spanObject.getComponentId()).componentName(spanObject.getComponent())
                        .spanLayer(spanObject.getSpanLayer().toString()).endTime(spanObject.getEndTime())
                        .startTime(spanObject.getStartTime()).spanType(spanObject.getSpanType().toString())
                        .peer(spanObject.getPeer()).peerId(spanObject.getPeerId()).operationId(spanObject.getOperationNameId());

                    for (LogMessage logMessage : spanObject.getLogsList()) {
                        spanBuilder.logEvent(logMessage.getDataList());
                    }

                    for (KeyWithStringValue tags : spanObject.getTagsList()) {
                        spanBuilder.tags(tags.getKey(), tags.getValue());
                    }

                    segmentBuilder.addSpan(spanBuilder.build());
                }

                for (TraceSegmentReference ref : traceSegmentObject.getRefsList()) {
                    Segment.SegmentRef segmentRef = Segment.SegmentRefBuilder.newBuilder(ref).build();
                    segmentBuilder.addRefs(segmentRef);
                }
                logger.info("segments :\n{} ", new Gson().toJson(segmentBuilder.build()));
            } catch (Exception e) {
            }
        }
        return this;
    }

    public long getSegmentCount() {
        return segmentCount.get();
    }
}
