package org.skywalking.apm.mock.collector.service;

import io.grpc.stub.StreamObserver;
import org.skywalking.apm.network.proto.ApplicationInstance;
import org.skywalking.apm.network.proto.ApplicationInstanceHeartbeat;
import org.skywalking.apm.network.proto.ApplicationInstanceMapping;
import org.skywalking.apm.network.proto.ApplicationInstanceRecover;
import org.skywalking.apm.network.proto.Downstream;
import org.skywalking.apm.network.proto.InstanceDiscoveryServiceGrpc;

public class MockInstanceDiscoveryService extends InstanceDiscoveryServiceGrpc.InstanceDiscoveryServiceImplBase {
    @Override
    public void registerRecover(ApplicationInstanceRecover request, StreamObserver<Downstream> responseObserver) {
        responseObserver.onNext(Downstream.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override public void heartbeat(ApplicationInstanceHeartbeat request, StreamObserver<Downstream> responseObserver) {
        responseObserver.onNext(Downstream.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void register(ApplicationInstance request, StreamObserver<ApplicationInstanceMapping> responseObserver) {
        responseObserver.onNext(ApplicationInstanceMapping.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
