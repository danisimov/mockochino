package com.github.danisimov.mockochino;

import com.fasterxml.jackson.databind.JsonNode;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.github.danisimov.mockochino.base.Asserts;
import com.github.danisimov.mockochino.base.UUIDTestBase;
import com.github.danisimov.mockochino.service.Const;
import com.github.danisimov.mockochino.service.SettingsManager;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import java.util.UUID;

/**
 * Created by danisimov on 8/6/19
 */
public class MockHandlerTest extends UUIDTestBase {

    /**
     * Mock tests
     */
    @Test
    public void postTest() {
        JsonNode responseMsg = target.path("mock/" + uuid).request().post(Entity.json(null), JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyString(responseMsg, Const.MESSAGE, Const.ECHO);
    }

    @Test
    public void putTest() {
        JsonNode responseMsg = target.path("mock/" + uuid).request()
                .put(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyString(responseMsg, Const.MESSAGE, Const.ECHO);
    }

    @Test
    public void patchTest() {
        Response responseMsg = target.path("mock/" + uuid).request().build("PATCH",
                Entity.json(buildTestJson(UUID.randomUUID().toString())))
                .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true).invoke();
        System.out.println(responseMsg);
        Assert.assertEquals(responseMsg.getStatus(), 200);
    }

    @Test
    public void authTest() {
        String token = UUID.randomUUID().toString();
        JsonNode responseMsg = target.path("settings/" + uuid).request().post(Entity.json(objMapper.createObjectNode()
                .put(Const.TOKEN, token)), JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);

        responseMsg = target.path("settings/" + uuid).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 1);

        JsonNode settingsNode = responseMsg.get(Const.DATA).get(0);
        Asserts.verifyString(settingsNode, Const.TOKEN, SettingsManager.getInstance().getByUUID(uuid).getToken());

        try {
            responseMsg = target.path("mock/" + uuid).request().post(Entity.json(null), JsonNode.class);
        } catch (NotAuthorizedException e) {
            responseMsg = e.getResponse().readEntity(JsonNode.class);
        }
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, false);
        Asserts.verifyString(responseMsg, Const.MESSAGE, "Authorization failed.");

        responseMsg = target.path("mock/" + uuid).request().header("Authorization", token)
                .post(Entity.json(null), JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyString(responseMsg, Const.MESSAGE, Const.ECHO);
    }
}
