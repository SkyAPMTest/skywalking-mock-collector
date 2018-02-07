package org.skywalking.apm.mock.collector.service;

import io.grpc.stub.StreamObserver;
import org.apache.skywalking.apm.network.proto.ServiceNameCollection;
import org.apache.skywalking.apm.network.proto.ServiceNameDiscoveryServiceGrpc;
import org.apache.skywalking.apm.network.proto.ServiceNameElement;
import org.apache.skywalking.apm.network.proto.ServiceNameMappingCollection;
import org.skywalking.apm.mock.collector.entity.RegistryItem;
import org.skywalking.apm.mock.collector.entity.ValidateData;

public class MockServiceNameDiscoveryService extends ServiceNameDiscoveryServiceGrpc.ServiceNameDiscoveryServiceImplBase {

    @Override
    public void discovery(ServiceNameCollection request,
        StreamObserver<ServiceNameMappingCollection> responseObserver) {
        for (ServiceNameElement element : request.getElementsList()) {
            ValidateData.INSTANCE.getRegistryItem().registryOperationName(new RegistryItem.OperationName(element.getApplicationId(),
                element.getServiceName(), element.getSrcSpanType()));
        }
        responseObserver.onNext(ServiceNameMappingCollection.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
