package org.skywalking.apm.mock.collector.service;

import io.grpc.stub.StreamObserver;
import org.apache.skywalking.apm.network.language.agent.ServiceNameCollection;
import org.apache.skywalking.apm.network.language.agent.ServiceNameDiscoveryServiceGrpc;
import org.apache.skywalking.apm.network.language.agent.ServiceNameElement;
import org.apache.skywalking.apm.network.language.agent.ServiceNameMappingCollection;
import org.skywalking.apm.mock.collector.entity.RegistryItem;
import org.skywalking.apm.mock.collector.entity.ValidateData;

public class MockServiceNameDiscoveryService extends ServiceNameDiscoveryServiceGrpc.ServiceNameDiscoveryServiceImplBase {

    @Override
    public void discovery(ServiceNameCollection request,
                          StreamObserver<ServiceNameMappingCollection> responseObserver) {
        for (ServiceNameElement element : request.getElementsList()) {
            ValidateData.INSTANCE.getRegistryItem().registryOperationName(new RegistryItem.OperationName(element.getApplicationId(),
                    element.getServiceName()));
        }
        responseObserver.onNext(ServiceNameMappingCollection.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
