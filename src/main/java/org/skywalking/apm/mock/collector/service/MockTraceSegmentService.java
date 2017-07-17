package org.skywalking.apm.mock.collector.service;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skywalking.apm.mock.collector.entity.Segment;
import org.skywalking.apm.mock.collector.entity.ValidateData;
import org.skywalking.apm.network.proto.Downstream;
import org.skywalking.apm.network.proto.KeyWithStringValue;
import org.skywalking.apm.network.proto.LogMessage;
import org.skywalking.apm.network.proto.SpanObject;
import org.skywalking.apm.network.proto.TraceSegmentObject;
import org.skywalking.apm.network.proto.TraceSegmentReference;
import org.skywalking.apm.network.proto.TraceSegmentServiceGrpc;
import org.skywalking.apm.network.proto.UpstreamSegment;

public class MockTraceSegmentService extends TraceSegmentServiceGrpc.TraceSegmentServiceImplBase {

    private Logger logger = LogManager.getLogger(MockTraceSegmentService.class);

    @Override
    public StreamObserver<UpstreamSegment> collect(final StreamObserver<Downstream> responseObserver) {
        return new StreamObserver<UpstreamSegment>() {
            public void onNext(UpstreamSegment value) {
                try {
                    TraceSegmentObject traceSegmentObject = TraceSegmentObject.parseFrom(value.getSegment());
                    Segment.SegmentBuilder segmentBuilder = Segment.SegmentBuilder.newBuilder(traceSegmentObject.getTraceSegmentId());
                    logger.debug("Receive segment: Application[{}], TraceSegmentId[{}]",
                        traceSegmentObject.getApplicationId(),
                        traceSegmentObject.getTraceSegmentId());

                    for (SpanObject spanObject : traceSegmentObject.getSpansList()) {
                        Segment.SpanBuilder spanBuilder = Segment.SpanBuilder.newBuilder(spanObject.getOperationName()).parentSpanId(spanObject.getParentSpanId())
                            .spanId(spanObject.getSpanId()).componentId(spanObject.getComponentId()).componentName(spanObject.getComponent())
                            .spanLayer(spanObject.getSpanLayer().toString()).endTime(spanObject.getEndTime())
                            .startTime(spanObject.getStartTime()).spanType(spanObject.getSpanType().toString())
                            .peer(spanObject.getPeer()).peerId(spanObject.getPeerId()).operationId(spanObject.getOperationNameId());

                        for (LogMessage logMessage : spanObject.getLogsList()) {
                            for (KeyWithStringValue pairValue : logMessage.getDataList()) {
                                spanBuilder.logs(Segment.LogEventBuilder.newBuilder().addLog(pairValue.getKey(),
                                    pairValue.getValue()).build());
                            }
                        }

                        for (KeyWithStringValue tags : spanObject.getTagsList()) {
                            spanBuilder.tags(tags.getKey(), tags.getValue());
                        }

                        segmentBuilder.addSpan(spanBuilder.build());
                    }

                    for (TraceSegmentReference ref : traceSegmentObject.getRefsList()) {
                        Segment.SegmentRef segmentRef = Segment.SegmentRefBuilder.newBuilder(ref.getParentTraceSegmentId())
                            .entryServiceName(ref.getEntryServiceName()).networkAddress(ref.getNetworkAddress())
                            .spanId(ref.getParentSpanId()).build();
                        segmentBuilder.addRefs(segmentRef);
                    }

                    ValidateData.INSTANCE.getSegmentItem().addSegmentItem(traceSegmentObject.getApplicationId(), segmentBuilder.build());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }

            public void onError(Throwable t) {

            }

            public void onCompleted() {
                responseObserver.onNext(Downstream.getDefaultInstance());
                responseObserver.onCompleted();
            }
        };
    }
}
