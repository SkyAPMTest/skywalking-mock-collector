package org.skywalking.apm.mock.collector.service;

import io.grpc.stub.StreamObserver;
import org.skywalking.apm.network.proto.Downstream;
import org.skywalking.apm.network.proto.JVMMetrics;
import org.skywalking.apm.network.proto.JVMMetricsServiceGrpc;

/**
 * Created by xin on 2017/7/11.
 */
public class MockJVMMetricsService extends JVMMetricsServiceGrpc.JVMMetricsServiceImplBase {
    @Override public void collect(JVMMetrics request, StreamObserver<Downstream> responseObserver) {
        responseObserver.onNext(Downstream.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
