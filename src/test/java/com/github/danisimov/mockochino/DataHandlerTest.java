package com.github.danisimov.mockochino;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.danisimov.mockochino.base.Asserts;
import com.github.danisimov.mockochino.base.UUIDTestBase;
import com.github.danisimov.mockochino.service.Const;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Entity;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * Created by danisimov on 8/6/19
 */
public class DataHandlerTest extends UUIDTestBase {

    /**
     * Data tests
     */
    @Test
    public void getRequestByUUIDTest() {
        JsonNode responseMsg = target.path("mock/" + uuid).request()
                .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);

        responseMsg = target.path("request/" + uuid).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        Assert.assertEquals(countStorage(), 1);
    }

    @Test
    public void getRequestByUUIDWithMultipleRowsTest() {
        UUID secondHandlerUUID = initSettings();

        JsonNode responseMsg = target.path("mock/" + uuid).request()
                .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);

        responseMsg = target.path("mock/" + secondHandlerUUID).request()
                .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);

        responseMsg = target.path("request/" + uuid).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        responseMsg = target.path("request/" + secondHandlerUUID).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        Assert.assertEquals(countStorage(), 2);
    }

    @Test
    public void getEmptyRequestsArrayTest() {
        JsonNode responseMsg;
        try {
            responseMsg = target.path("request/" + uuid).request().get(JsonNode.class);
        } catch (NotFoundException e) {
            responseMsg = e.getResponse().readEntity(JsonNode.class);
        }
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 0);

        Assert.assertEquals(countStorage(), 0);
    }

    @Test
    public void countTest() {
        JsonNode responseMsg = target.path("mock/" + uuid).request()
                .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);

        responseMsg = target.path("request/count").request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyInteger(responseMsg, Const.AMOUNT, 1);

        Assert.assertEquals(countStorage(), 1);
    }

    @Test
    public void countByUUIDTest() {
        UUID secondHandlerUUID = initSettings();
        UUID thirdHandlerUUID = initSettings();

        JsonNode responseMsg = target.path("mock/" + uuid).request()
                .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);

        responseMsg = target.path("mock/" + secondHandlerUUID).request()
                .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);

        responseMsg = target.path("request/count/" + uuid).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyInteger(responseMsg, Const.AMOUNT, 1);

        responseMsg = target.path("request/count/" + secondHandlerUUID).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyInteger(responseMsg, Const.AMOUNT, 1);

        responseMsg = target.path("request/count/" + thirdHandlerUUID).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyInteger(responseMsg, Const.AMOUNT, 0);

        Assert.assertEquals(countStorage(), 2);
    }

    @Test
    public void deleteTest() {
        JsonNode responseMsg = target.path("mock/" + uuid).request()
                .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);
        Assert.assertEquals(countStorage(), 1);

        UUID secondHandlerUUID = initSettings();
        responseMsg = target.path("mock/" + secondHandlerUUID).request()
                .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);
        Assert.assertEquals(countStorage(), 2);

        responseMsg = target.path("request").request().delete(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);

        Assert.assertEquals(countStorage(), 0);
    }

    @Test
    public void deleteByUUIDTest() {
        JsonNode responseMsg = target.path("mock/" + uuid).request()
                .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);
        Assert.assertEquals(countStorage(), 1);
        Assert.assertEquals(countStorageByUUID(uuid), 1);

        UUID secondHandlerUUID = initSettings();
        responseMsg = target.path("mock/" + secondHandlerUUID).request()
                .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);
        Assert.assertEquals(countStorage(), 2);
        Assert.assertEquals(countStorageByUUID(uuid), 1);
        Assert.assertEquals(countStorageByUUID(secondHandlerUUID), 1);

        responseMsg = target.path("request/" + uuid).request().delete(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);

        Assert.assertEquals(countStorage(), 1);
        Assert.assertEquals(countStorageByUUID(uuid), 0);
        Assert.assertEquals(countStorageByUUID(secondHandlerUUID), 1);
    }
}
