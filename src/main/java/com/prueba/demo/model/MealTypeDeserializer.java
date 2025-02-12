package com.prueba.demo.model;

import com.fasterxml.jackson.databind.JsonDeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MealTypeDeserializer extends JsonDeserializer<List<Integer>> {
    @Override
    public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (p.isExpectedStartArrayToken()) {
            // If the value is a list, parse normally
            return p.readValueAs(List.class);
        } else {
            // If the value is a single integer, wrap it in a list
            return Collections.singletonList(p.getIntValue());
        }
    }
}
