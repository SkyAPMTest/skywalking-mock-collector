package org.skywalking.apm.mock.collector.service;

import io.grpc.stub.StreamObserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.skywalking.apm.network.language.agent.Application;
import org.apache.skywalking.apm.network.language.agent.ApplicationMapping;
import org.apache.skywalking.apm.network.language.agent.ApplicationRegisterServiceGrpc;
import org.apache.skywalking.apm.network.language.agent.KeyWithIntegerValue;
import org.skywalking.apm.mock.collector.entity.RegistryItem;
import org.skywalking.apm.mock.collector.entity.ValidateData;

import static org.skywalking.apm.mock.collector.service.Sequences.SERVICE_MAPPING;
import static org.skywalking.apm.mock.collector.service.Sequences.ENDPOINT_SEQUENCE;

/**
 * Created by xin on 2017/7/11.
 */
public class MockApplicationRegisterService extends ApplicationRegisterServiceGrpc.ApplicationRegisterServiceImplBase {
    private Logger logger = LogManager.getLogger(MockTraceSegmentService.class);

    @Override
    public void applicationCodeRegister(Application request, StreamObserver<ApplicationMapping> responseObserver) {
        logger.debug("receive application register.");
        String applicationCode = request.getApplicationCode();
        ApplicationMapping.Builder builder = ApplicationMapping.newBuilder();

        if (applicationCode.startsWith("localhost") || applicationCode.startsWith("127.0.0.1") || applicationCode.contains(":") || applicationCode.contains("/")) {
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            return;
        }

        Integer applicationId = SERVICE_MAPPING.get(applicationCode);
        if (applicationId == null) {
            applicationId = ENDPOINT_SEQUENCE.incrementAndGet();
            SERVICE_MAPPING.put(applicationCode, applicationId);
            ValidateData.INSTANCE.getRegistryItem().registryApplication(new RegistryItem.Application(applicationCode,
                    applicationId));
        }

        builder.setApplication(KeyWithIntegerValue.newBuilder().setKey(applicationCode).setValue(applicationId));
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
