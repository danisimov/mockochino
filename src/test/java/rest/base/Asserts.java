package rest.base;

import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;

/**
 * Created by danisimov on 8/6/19
 */
public class Asserts {

    /**
     * Verification by value.
     */
    public static void verifyArray(JsonNode node, String name, int size) {
        verifyArray(node, name);
        Assert.assertEquals(node.get(name).size(), size);
    }

    public static void verifyBoolean(JsonNode node, String name, boolean value) {
        verifyBoolean(node, name);
        Assert.assertEquals(node.get(name).asBoolean(), value);
    }

    public static void verifyInteger(JsonNode node, String name, int value) {
        verifyInteger(node, name);
        Assert.assertEquals(node.get(name).asInt(), value);
    }

    public static void verifyJsonObject(JsonNode node, String name, JsonNode json) {
        verifyJsonObject(node, name);
        Assert.assertEquals(node.get(name), json);
    }

    public static void verifyString(JsonNode node, String name, String value) {
        verifyString(node, name);
        Assert.assertEquals(node.get(name).asText(), value);
    }

    /**
     * Verification by type.
     */
    public static void verifyArray(JsonNode node, String name) {
        Assert.assertTrue(node.has(name));
        Assert.assertTrue(node.get(name).isArray());
    }

    private static void verifyBoolean(JsonNode node, String name) {
        Assert.assertTrue(node.has(name));
        Assert.assertTrue(node.get(name).isBoolean());
    }

    static void verifyInteger(JsonNode node, String name) {
        Assert.assertTrue(node.has(name));
        Assert.assertTrue(node.get(name).isInt());
    }

    private static void verifyJsonObject(JsonNode node, String name) {
        Assert.assertTrue(node.has(name));
        Assert.assertTrue(node.get(name).isObject());
    }

    public static void verifyString(JsonNode node, String name) {
        Assert.assertTrue(node.has(name));
        Assert.assertTrue(node.get(name).isTextual());
    }
}
