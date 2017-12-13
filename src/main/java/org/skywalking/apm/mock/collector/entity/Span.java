/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.mock.collector.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.skywalking.apm.network.proto.KeyWithStringValue;
import org.apache.skywalking.apm.network.proto.TraceSegmentReference;
import org.apache.skywalking.apm.network.proto.UniqueId;

@Builder
@ToString
@AllArgsConstructor
public class Span {
    private String operationName;
    private int operationId;
    private int parentSpanId;
    private int spanId;
    private String spanLayer;
    private long startTime;
    private long endTime;
    private int componentId;
    private String componentName;
    private boolean isError;
    private String spanType;
    private String peer;
    private int peerId;
    private List<KeyValuePair> tags = new ArrayList<>();
    private List<LogEvent> logs = new ArrayList<>();
    private List<SegmentRef> refs = new ArrayList<>();

    public static class LogEvent {
        private List<KeyValuePair> logEvent;

        public LogEvent() {
            this.logEvent = new ArrayList<>();
        }
    }

    public static class SpanBuilder {
        public SpanBuilder logEvent(List<KeyWithStringValue> eventMessage) {
            if (logs == null) {
                logs = new ArrayList<>();
            }

            LogEvent event = new LogEvent();
            for (KeyWithStringValue value : eventMessage) {
                event.logEvent.add(new KeyValuePair(value.getKey(), value.getValue()));
            }
            logs.add(event);
            return this;
        }

        public SpanBuilder tags(String key, String value) {
            if (tags == null) {
                tags = new ArrayList<>();
            }

            tags.add(new KeyValuePair(key, value));
            return this;
        }

        public SpanBuilder ref(SegmentRef segmentRefBuilder) {
            if (refs == null) {
                refs = new ArrayList<>();
            }

            refs.add(segmentRefBuilder);
            return this;
        }

    }

    public static class KeyValuePair {
        @Getter
        private String key;
        @Getter
        private String value;

        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    @ToString
    public static class SegmentRef {
        @Getter
        private int parentServiceId;
        @Getter
        private String parentServiceName;
        @Getter
        private int networkAddressId;
        @Getter
        private int entryServiceId;
        @Getter
        private String refType;
        @Getter
        private int parentSpanId;
        @Getter
        private String parentTraceSegmentId;
        @Getter
        private int parentApplicationInstanceId;
        @Getter
        private String networkAddress;
        @Getter
        private String entryServiceName;
        @Getter
        private int entryApplicationInstanceId;

        public SegmentRef(TraceSegmentReference ref) {
            UniqueId uniqueId = ref.getParentTraceSegmentId();
            this.parentTraceSegmentId = uniqueId.getIdParts(0) + "" + uniqueId.getIdParts(1) + "" + uniqueId.getIdParts(2);
            this.refType = ref.getRefType().toString();
            this.parentSpanId = ref.getParentSpanId();
            this.entryServiceId = ref.getEntryServiceId();
            this.networkAddressId = ref.getNetworkAddressId();
            this.parentApplicationInstanceId = ref.getParentApplicationInstanceId();
            this.parentServiceId = ref.getParentServiceId();
            this.parentServiceName = ref.getParentServiceName();
            this.networkAddress = ref.getNetworkAddress();
            this.entryServiceName = ref.getEntryServiceName();
            this.entryApplicationInstanceId = ref.getEntryApplicationInstanceId();
        }
    }
}
