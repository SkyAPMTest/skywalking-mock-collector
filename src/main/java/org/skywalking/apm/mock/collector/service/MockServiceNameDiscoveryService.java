package org.skywalking.apm.mock.collector.service;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.skywalking.apm.network.proto.ServiceNameCollection;
import org.apache.skywalking.apm.network.proto.ServiceNameDiscoveryServiceGrpc;
import org.apache.skywalking.apm.network.proto.ServiceNameElement;
import org.apache.skywalking.apm.network.proto.ServiceNameMappingCollection;
import org.apache.skywalking.apm.network.proto.ServiceNameMappingElement;
import org.skywalking.apm.mock.collector.entity.RegistryItem;
import org.skywalking.apm.mock.collector.entity.ValidateData;

public class MockServiceNameDiscoveryService extends ServiceNameDiscoveryServiceGrpc.ServiceNameDiscoveryServiceImplBase {
    private Logger logger = LogManager.getLogger(MockTraceSegmentService.class);
    private AtomicInteger operationNameSequence = new AtomicInteger();

    @Override
    public void discovery(ServiceNameCollection request,
        StreamObserver<ServiceNameMappingCollection> responseObserver) {
        logger.debug("Receive service name");
        ServiceNameMappingCollection.Builder builder = ServiceNameMappingCollection.newBuilder();
        for (ServiceNameElement element : request.getElementsList()) {
            builder.addElements(ServiceNameMappingElement.newBuilder().setElement(ServiceNameElement.
                newBuilder().setApplicationId(element.getApplicationId()).setServiceName(element.getServiceName()))
                .setServiceId(operationNameSequence.incrementAndGet()).build());
            ValidateData.INSTANCE.getRegistryItem().registryOperationName(new RegistryItem.OperationName(element.getApplicationId(),
                element.getServiceName()));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
