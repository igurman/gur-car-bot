package com.igurman.gur_car_bot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.igurman.gur_car_bot.exception.ParserRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ObjectMapperProvider {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String toJson(Object source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new ParserRuntimeException("*** Не удалось преобразовать объект в json: " + e + " source: " + source);
        }
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void modify(JsonNode rootNode, String lv1, String lv2, Integer data) {
        ((ObjectNode) rootNode.path(lv1)).put(lv2, data);
    }

    public void modify(JsonNode rootNode, String lv1, String lv2, String lv3, String lv4, String data) {
        ((ObjectNode) rootNode.path(lv1).get(lv2).get(lv3)).put(lv4, data);
    }

    public JsonNode readJsonFile(String resourcePath) {
        try {
            return this.getObjectMapper()
                    .readTree(new ClassPathResource(resourcePath).getInputStream());
        } catch (IOException e) {
            throw new ParserRuntimeException("*** Не смог прочитать или распарсить файл: " + e + " путь: " + resourcePath);
        }
    }

    public JsonNode textToJsonModel(String text) {
        try {
            return this.getObjectMapper().readTree(text);
        } catch (JsonProcessingException e) {
            throw new ParserRuntimeException("*** Не смог перевести в Json Object текст: " + e + " \ntext: \n" + text);
        }
    }

}
