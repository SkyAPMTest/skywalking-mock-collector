package org.skywalking.apm.mock.collector.service;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.skywalking.apm.network.proto.Application;
import org.skywalking.apm.network.proto.ApplicationMapping;
import org.skywalking.apm.network.proto.ApplicationRegisterServiceGrpc;
import org.skywalking.apm.network.proto.KeyWithIntegerValue;

/**
 * Created by xin on 2017/7/11.
 */
public class MockApplicationRegisterService extends ApplicationRegisterServiceGrpc.ApplicationRegisterServiceImplBase {
    private AtomicInteger currentId = new AtomicInteger(1);
    private ConcurrentHashMap<String, Integer> applicationMapping = new ConcurrentHashMap<String, Integer>();

    @Override
    public void register(Application request, StreamObserver<ApplicationMapping> responseObserver) {
        ApplicationMapping.Builder builder = ApplicationMapping.newBuilder();
        for (String applicationCode : request.getApplicationCodeList()) {
            Integer applicationId = applicationMapping.get(applicationCode);
            if (applicationId == null) {
                applicationId = currentId.incrementAndGet();
                applicationMapping.put(applicationCode, applicationId);
            }

            builder.addApplication(KeyWithIntegerValue.newBuilder().setKey(applicationCode).setValue(applicationId));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

}
