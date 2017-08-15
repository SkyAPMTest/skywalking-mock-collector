package org.skywalking.apm.mock.collector.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SegmentItems {
    private Logger logger = LogManager.getLogger(SegmentItem.class);

    private AtomicLong segmentCount = new AtomicLong();
    private Map<String, SegmentItem> segmentItems;

    public SegmentItems() {
        this.segmentItems = new HashMap<>();
    }

    public SegmentItems addSegmentItem() {
        long currentSegmentCount = segmentCount.incrementAndGet();
        if (currentSegmentCount % 200 == 0) {
            logger.info("current segment size: {}", currentSegmentCount);
        }
        return this;
    }

    public long getSegmentCount() {
        return segmentCount.get();
    }
}
