package org.skywalking.apm.mock.collector.service;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.skywalking.apm.network.proto.ApplicationInstance;
import org.apache.skywalking.apm.network.proto.ApplicationInstanceHeartbeat;
import org.apache.skywalking.apm.network.proto.ApplicationInstanceMapping;
import org.apache.skywalking.apm.network.proto.Downstream;
import org.apache.skywalking.apm.network.proto.InstanceDiscoveryServiceGrpc;
import org.skywalking.apm.mock.collector.entity.RegistryItem;
import org.skywalking.apm.mock.collector.entity.ValidateData;

public class MockInstanceDiscoveryService extends InstanceDiscoveryServiceGrpc.InstanceDiscoveryServiceImplBase {
    private AtomicInteger instanceSequence = new AtomicInteger();

    @Override public void heartbeat(ApplicationInstanceHeartbeat request, StreamObserver<Downstream> responseObserver) {
        ValidateData.INSTANCE.getRegistryItem().registryHeartBeat(new RegistryItem.HeartBeat(request.getApplicationInstanceId()));
        responseObserver.onNext(Downstream.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override public void registerInstance(ApplicationInstance request,
        StreamObserver<ApplicationInstanceMapping> responseObserver) {
        int instanceId = instanceSequence.incrementAndGet();
        ValidateData.INSTANCE.getRegistryItem().registryInstance(new RegistryItem.Instance(request.getApplicationId(), instanceId));

        responseObserver.onNext(ApplicationInstanceMapping.newBuilder().setApplicationId(request.getApplicationId())
            .setApplicationInstanceId(instanceId).build());
        responseObserver.onCompleted();
    }
}
