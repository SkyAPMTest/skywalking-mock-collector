package org.skywalking.apm.mock.collector.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Sequences {

    public static final AtomicInteger INSTANCE_SEQUENCE = new AtomicInteger();

    public static final AtomicInteger SERVICE_SEQUENCE = new AtomicInteger(1);

    public static final ConcurrentHashMap<String, Integer> APPLICATION_MAPPING = new ConcurrentHashMap<String, Integer>();
}
