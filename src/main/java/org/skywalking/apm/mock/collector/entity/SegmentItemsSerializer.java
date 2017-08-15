package org.skywalking.apm.mock.collector.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class SegmentItemsSerializer implements JsonSerializer<SegmentItems> {

    @Override
    public JsonElement serialize(SegmentItems src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject applicationSegmentItems = new JsonObject();
        applicationSegmentItems.addProperty("total segment count", src.getSegmentCount());
        return applicationSegmentItems;
    }
}
