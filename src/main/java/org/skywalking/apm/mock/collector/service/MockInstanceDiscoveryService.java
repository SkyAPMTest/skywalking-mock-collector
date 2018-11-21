package org.skywalking.apm.mock.collector.service;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.skywalking.apm.network.language.agent.*;
import org.skywalking.apm.mock.collector.entity.RegistryItem;
import org.skywalking.apm.mock.collector.entity.ValidateData;

import static org.skywalking.apm.mock.collector.service.Sequences.INSTANCE_SEQUENCE;

public class MockInstanceDiscoveryService extends InstanceDiscoveryServiceGrpc.InstanceDiscoveryServiceImplBase {


    @Override
    public void heartbeat(ApplicationInstanceHeartbeat request, StreamObserver<Downstream> responseObserver) {
        ValidateData.INSTANCE.getRegistryItem().registryHeartBeat(new RegistryItem.HeartBeat(request.getApplicationInstanceId()));
        responseObserver.onNext(Downstream.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void registerInstance(ApplicationInstance request,
                                 StreamObserver<ApplicationInstanceMapping> responseObserver) {
        int instanceId = INSTANCE_SEQUENCE.incrementAndGet();
        ValidateData.INSTANCE.getRegistryItem().registryInstance(new RegistryItem.Instance(request.getApplicationId(), instanceId));

        responseObserver.onNext(ApplicationInstanceMapping.newBuilder().setApplicationId(request.getApplicationId())
                .setApplicationInstanceId(instanceId).build());
        responseObserver.onCompleted();
    }
}
