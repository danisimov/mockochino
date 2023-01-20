package com.github.danisimov.mockochino;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.danisimov.mockochino.base.Asserts;
import com.github.danisimov.mockochino.base.UUIDTestBase;
import com.github.danisimov.mockochino.service.Const;
import com.github.danisimov.mockochino.service.Settings;
import com.github.danisimov.mockochino.service.SettingsManager;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Entity;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * Created by danisimov on 8/6/19
 */
public class SettingsHandlerTest extends UUIDTestBase {

    /**
     * Settings tests
     */
    @Test
    public void initTest() {
        JsonNode responseMsg = target.path("settings/init").request().post(null, JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyString(responseMsg, Const.UUID);
        String uuid = responseMsg.get(Const.UUID).asText();
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new AssertionError("[UUID]-[" + uuid + "] is not valid");
        }
    }

    @Test
    public void getSettingsTest() {
        JsonNode responseMsg = target.path("settings").request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        JsonNode settingsNode = responseMsg.get(Const.DATA).get(0);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(uuid));
        Settings settings = SettingsManager.getInstance().getByUUID(uuid);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(settings.getUuid()));
        Asserts.verifyInteger(settingsNode, Const.DELAY, settings.getDelay());
        Asserts.verifyInteger(settingsNode, Const.CODE, settings.getCode());
        Asserts.verifyString(settingsNode, Const.TOKEN, settings.getToken());
        Asserts.verifyJsonObject(settingsNode, Const.RESPONSE, settings.getResponse());
    }

    @Test
    public void getSettingsWithMultipleRowsTest() {
        initSettings();

        JsonNode responseMsg = target.path("settings").request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 2);
    }

    @Test
    public void getSettingsByUUIDTest() {
        JsonNode responseMsg = target.path("settings/" + uuid).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        JsonNode settingsNode = responseMsg.get(Const.DATA).get(0);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(uuid));
        Settings settings = SettingsManager.getInstance().getByUUID(uuid);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(settings.getUuid()));
        Asserts.verifyInteger(settingsNode, Const.DELAY, settings.getDelay());
        Asserts.verifyInteger(settingsNode, Const.CODE, settings.getCode());
        Asserts.verifyString(settingsNode, Const.TOKEN, settings.getToken());
        Asserts.verifyJsonObject(settingsNode, Const.RESPONSE, settings.getResponse());
    }

    @Test
    public void updateSettingsTest() {
        int delay = 2;
        int code = 404;
        String token = UUID.randomUUID().toString();
        JsonNode response = objMapper.createObjectNode().put(Const.RESULT, "fake");
        JsonNode responseMsg = target.path("settings/" + uuid).request().post(Entity.json(objMapper.createObjectNode()
                .put(Const.DELAY, delay)
                .put(Const.CODE, code)
                .put(Const.TOKEN, token)
                .set(Const.RESPONSE, response)), JsonNode.class);
        System.out.println(responseMsg);

        responseMsg = target.path("settings/" + uuid).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        JsonNode settingsNode = responseMsg.get(Const.DATA).get(0);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(uuid));
        Asserts.verifyInteger(settingsNode, Const.DELAY, delay);
        Asserts.verifyInteger(settingsNode, Const.CODE, code);
        Asserts.verifyString(settingsNode, Const.TOKEN, token);
        Asserts.verifyJsonObject(settingsNode, Const.RESPONSE, response);
    }

    @Test
    public void resetSettingsTest() {
        int delay = 2;
        int code = 404;
        String token = UUID.randomUUID().toString();
        JsonNode response = objMapper.createObjectNode().put(Const.RESULT, "fake");
        JsonNode responseMsg = target.path("settings/" + uuid).request().post(Entity.json(objMapper.createObjectNode()
                .put(Const.DELAY, delay)
                .put(Const.CODE, code)
                .put(Const.TOKEN, token)
                .set(Const.RESPONSE, response)), JsonNode.class);
        System.out.println(responseMsg);

        responseMsg = target.path("settings/" + uuid).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        JsonNode settingsNode = responseMsg.get(Const.DATA).get(0);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(uuid));
        Asserts.verifyInteger(settingsNode, Const.DELAY, delay);
        Asserts.verifyInteger(settingsNode, Const.CODE, code);
        Asserts.verifyString(settingsNode, Const.TOKEN, token);
        Asserts.verifyJsonObject(settingsNode, Const.RESPONSE, response);

        responseMsg = target.path("settings/reset/" + uuid).request().post(null, JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);

        delay = 0;
        code = 200;
        token = "";
        response = objMapper.createObjectNode()
                .put(Const.RESULT, true)
                .put(Const.MESSAGE, Const.ECHO);

        responseMsg = target.path("settings/" + uuid).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        settingsNode = responseMsg.get(Const.DATA).get(0);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(uuid));
        Asserts.verifyInteger(settingsNode, Const.DELAY, delay);
        Asserts.verifyInteger(settingsNode, Const.CODE, code);
        Asserts.verifyString(settingsNode, Const.TOKEN, token);
        Asserts.verifyJsonObject(settingsNode, Const.RESPONSE, response);
    }

    @Test
    public void deleteByUUIDTest() {
        JsonNode responseMsg = target.path("settings/" + uuid).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        JsonNode settingsNode = responseMsg.get(Const.DATA).get(0);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(uuid));
        Settings settings = SettingsManager.getInstance().getByUUID(uuid);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(settings.getUuid()));
        Asserts.verifyInteger(settingsNode, Const.DELAY, settings.getDelay());
        Asserts.verifyInteger(settingsNode, Const.CODE, settings.getCode());
        Asserts.verifyString(settingsNode, Const.TOKEN, settings.getToken());
        Asserts.verifyJsonObject(settingsNode, Const.RESPONSE, settings.getResponse());

        UUID secondHandlerUUID = initSettings();
        responseMsg = target.path("settings/" + secondHandlerUUID).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        settingsNode = responseMsg.get(Const.DATA).get(0);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(secondHandlerUUID));
        Settings secondSettings = SettingsManager.getInstance().getByUUID(secondHandlerUUID);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(secondSettings.getUuid()));
        Asserts.verifyInteger(settingsNode, Const.DELAY, secondSettings.getDelay());
        Asserts.verifyInteger(settingsNode, Const.CODE, secondSettings.getCode());
        Asserts.verifyString(settingsNode, Const.TOKEN, secondSettings.getToken());
        Asserts.verifyJsonObject(settingsNode, Const.RESPONSE, secondSettings.getResponse());

        responseMsg = target.path("settings/" + uuid).request().delete(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);

        try {
            responseMsg = target.path("settings/" + uuid).request().get(JsonNode.class);
        } catch (NotFoundException e) {
            responseMsg = e.getResponse().readEntity(JsonNode.class);
        }
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, false);
        Asserts.verifyString(responseMsg, Const.MESSAGE, Const.buildNoSuchSettingsMessage(uuid));

        responseMsg = target.path("settings/" + secondHandlerUUID).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        settingsNode = responseMsg.get(Const.DATA).get(0);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(secondHandlerUUID));
        secondSettings = SettingsManager.getInstance().getByUUID(secondHandlerUUID);
        Asserts.verifyString(settingsNode, Const.UUID, String.valueOf(secondSettings.getUuid()));
        Asserts.verifyInteger(settingsNode, Const.DELAY, secondSettings.getDelay());
        Asserts.verifyInteger(settingsNode, Const.CODE, secondSettings.getCode());
        Asserts.verifyString(settingsNode, Const.TOKEN, secondSettings.getToken());
        Asserts.verifyJsonObject(settingsNode, Const.RESPONSE, secondSettings.getResponse());
    }
}
