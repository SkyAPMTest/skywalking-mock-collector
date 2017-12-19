package org.skywalking.apm.mock.collector.service;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.skywalking.apm.network.proto.Applications;
import org.apache.skywalking.apm.network.proto.ApplicationMappings;
import org.apache.skywalking.apm.network.proto.ApplicationRegisterServiceGrpc;
import org.apache.skywalking.apm.network.proto.KeyWithIntegerValue;
import org.skywalking.apm.mock.collector.entity.RegistryItem;
import org.skywalking.apm.mock.collector.entity.ValidateData;

/**
 * Created by xin on 2017/7/11.
 */
public class MockApplicationRegisterService extends ApplicationRegisterServiceGrpc.ApplicationRegisterServiceImplBase {
    private Logger logger = LogManager.getLogger(MockTraceSegmentService.class);
    private AtomicInteger currentId = new AtomicInteger(1);
    private ConcurrentHashMap<String, Integer> applicationMapping = new ConcurrentHashMap<String, Integer>();

    @Override public void batchRegister(Applications request, StreamObserver<ApplicationMappings> responseObserver) {
        logger.debug("receive application register.");
        ApplicationMappings.Builder builder = ApplicationMappings.newBuilder();
        for (String applicationCode : request.getApplicationCodesList()) {
            if (applicationCode.startsWith("localhost") || applicationCode.startsWith("127.0.0.1") || applicationCode.contains(":")) {
                continue;
            }
            Integer applicationId = applicationMapping.get(applicationCode);
            if (applicationId == null) {
                applicationId = currentId.incrementAndGet();
                applicationMapping.put(applicationCode, applicationId);
                ValidateData.INSTANCE.getRegistryItem().registryApplication(new RegistryItem.Application(applicationCode,
                    applicationId));
            }

            builder.addApplications(KeyWithIntegerValue.newBuilder().setKey(applicationCode).setValue(applicationId));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
