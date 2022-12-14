package org.letter.metrics.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.InputStream;

/**
 * JsonUtil jackson序列化
 *
 * @author wuhao
 */
public class JsonUtil {
    private static final ObjectMapper defaultObjectMapper = new ObjectMapper();
    private static ObjectMapper objectMapper = null;

    public JsonUtil() {}

    private static ObjectMapper mapper() {
        return objectMapper == null ? defaultObjectMapper : objectMapper;
    }

    public static JsonNode toJson(Object var0) {
        try {
            return mapper().valueToTree(var0);
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static <A> A fromJson(JsonNode var0, Class<A> var1) {
        try {
            return mapper().treeToValue(var0, var1);
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static ObjectNode newObject() {
        return mapper().createObjectNode();
    }

    public static String stringify(JsonNode var0) {
        return var0.toString();
    }

    public static JsonNode parse(String var0) {
        try {
            return (JsonNode)mapper().readValue(var0, JsonNode.class);
        } catch (Throwable var2) {
            throw new RuntimeException(var2);
        }
    }

    public static JsonNode parse(InputStream var0) {
        try {
            return (JsonNode)mapper().readValue(var0, JsonNode.class);
        } catch (Throwable var2) {
            throw new RuntimeException(var2);
        }
    }

    public static void setObjectMapper(ObjectMapper var0) {
        objectMapper = var0;
    }
}
