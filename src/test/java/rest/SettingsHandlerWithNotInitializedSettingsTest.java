package rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.testng.annotations.Test;
import rest.base.Asserts;
import rest.base.ServerTestBase;
import rest.service.Const;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Entity;
import java.util.UUID;

/**
 * Created by danisimov on 8/6/19
 */
public class SettingsHandlerWithNotInitializedSettingsTest extends ServerTestBase {

    /**
     * Settings tests
     */
    @Test
    public void getEmptySettingsArrayTest() {
        JsonNode responseMsg;
        try {
            responseMsg = target.path("settings").request().get(JsonNode.class);
        } catch (NotFoundException e) {
            responseMsg = e.getResponse().readEntity(JsonNode.class);
        }
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 0);
    }

    @Test
    public void getSettingsByUUIDWhichIsNotInitializedTest() {
        UUID uuid = UUID.randomUUID();
        JsonNode responseMsg;
        try {
            responseMsg = target.path("settings/" + uuid).request().get(JsonNode.class);
        } catch (NotFoundException e) {
            responseMsg = e.getResponse().readEntity(JsonNode.class);
        }
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, false);
        Asserts.verifyString(responseMsg, Const.MESSAGE, Const.buildNoSuchSettingsMessage(uuid));
    }

    @Test
    public void updateSettingsWhichIsNotInitializedTest() {
        UUID uuid = UUID.randomUUID();
        JsonNode responseMsg;
        try {
            responseMsg = target.path("settings/" + uuid).request().post(Entity.json(objMapper.createObjectNode()), JsonNode.class);
        } catch (NotFoundException e) {
            responseMsg = e.getResponse().readEntity(JsonNode.class);
        }
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, false);
        Asserts.verifyString(responseMsg, Const.MESSAGE, Const.buildNoSuchSettingsMessage(uuid));
    }

    @Test
    public void resetSettingsWhichIsNotInitializedTest() {
        UUID uuid = UUID.randomUUID();
        JsonNode responseMsg;
        try {
            responseMsg = target.path("settings/reset/" + uuid).request().post(null, JsonNode.class);
        } catch (NotFoundException e) {
            responseMsg = e.getResponse().readEntity(JsonNode.class);
        }
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, false);
        Asserts.verifyString(responseMsg, Const.MESSAGE, Const.buildNoSuchSettingsMessage(uuid));
    }
}
