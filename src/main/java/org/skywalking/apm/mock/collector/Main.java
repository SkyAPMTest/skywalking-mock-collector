package org.skywalking.apm.mock.collector;

import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.local.LocalAddress;
import java.net.InetSocketAddress;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.skywalking.apm.mock.collector.service.ReceiveDataService;
import org.skywalking.apm.mock.collector.service.GrpcAddressHttpService;
import org.skywalking.apm.mock.collector.service.MockApplicationRegisterService;
import org.skywalking.apm.mock.collector.service.MockInstanceDiscoveryService;
import org.skywalking.apm.mock.collector.service.MockJVMMetricsService;
import org.skywalking.apm.mock.collector.service.MockTraceSegmentService;

public class Main {
    public static void main(String[] args) throws Exception {
        NettyServerBuilder.forAddress(LocalAddress.ANY).forPort(19876)
            .maxConcurrentCallsPerConnection(12).maxMessageSize(16777216)
            .addService(new MockApplicationRegisterService())
            .addService(new MockInstanceDiscoveryService())
            .addService(new MockJVMMetricsService())
            .addService(new MockTraceSegmentService()).build().start();

        Server jettyServer = new Server(new InetSocketAddress("0.0.0.0",
            Integer.valueOf(12800)));
        String contextPath = "/";
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath(contextPath);
        servletContextHandler.addServlet(GrpcAddressHttpService.class, GrpcAddressHttpService.SERVLET_PATH);
        servletContextHandler.addServlet(ReceiveDataService.class, ReceiveDataService.SERVLET_PATH);
        jettyServer.setHandler(servletContextHandler);
        jettyServer.start();
    }
}
