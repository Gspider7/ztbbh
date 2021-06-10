package com.acrobat.ztb.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.List;

/**
 * json序列化工具类
 *
 * @author xutao
 * @date 2019-08-12 15:50
 */
public class JacksonUtil {

    private static ObjectMapper objectMapper = null;

    public static ObjectMapper objectMapper() {
        if (objectMapper != null) return objectMapper;
        synchronized (JacksonUtil.class) {
            if (objectMapper == null) {
                objectMapper = new Jackson2ObjectMapperBuilder().createXmlMapper(false).build();

                objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

//                // null值序列化
//                objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//                    @Override
//                    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
//                            throws IOException {
//                        jsonGenerator.writeString("");
//                    }
//                });
            }
        }
        return objectMapper;
    }

    // ------------------------------------------------------------------------

    public static <T> T readValue(String content, Class<T> clazz) throws IOException {
        return objectMapper().readValue(content, clazz);
    }

    public static <T> List<T> readListValues(String content, Class<T> clazz) throws IOException {
        JavaType javaType = objectMapper().getTypeFactory().constructCollectionType(List.class, clazz);
        return objectMapper().readValue(content, javaType);
    }

    public static String writeValueAsString(Object obj) throws JsonProcessingException {
        return objectMapper().writeValueAsString(obj);
    }

    public static JsonNode readTree(String json) throws IOException {
        return objectMapper().readTree(json);
    }

    // ------------------------------------------------------------------------

    public static void main(String[] args) throws IOException {
        String s = "[\"abc\", \"def\", \"g1\", 123]";

        List<Object> list = JacksonUtil.readListValues(s, Object.class);
        System.out.println();
    }
}
