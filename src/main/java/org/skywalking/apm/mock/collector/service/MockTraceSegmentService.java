package org.skywalking.apm.mock.collector.service;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import org.skywalking.apm.network.proto.Downstream;
import org.skywalking.apm.network.proto.TraceSegmentObject;
import org.skywalking.apm.network.proto.TraceSegmentServiceGrpc;
import org.skywalking.apm.network.proto.UpstreamSegment;

public class MockTraceSegmentService extends TraceSegmentServiceGrpc.TraceSegmentServiceImplBase {

    @Override
    public StreamObserver<UpstreamSegment> collect(final StreamObserver<Downstream> responseObserver) {
        return new StreamObserver<UpstreamSegment>() {
            public void onNext(UpstreamSegment value) {
                try {
                    System.out.println(new Gson().toJson(TraceSegmentObject.parseFrom(value.getSegment())));
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
