package com.github.danisimov.mockochino;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.danisimov.mockochino.base.Asserts;
import com.github.danisimov.mockochino.base.ServerTestBase;
import com.github.danisimov.mockochino.service.Const;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * Created by danisimov on 8/6/19
 */
public class MockHandlerWithNotInitializedSettingsTest extends ServerTestBase {

    /**
     * Mock tests
     */
    @Test
    public void postTest() {
        UUID uuid = UUID.randomUUID();
        JsonNode responseMsg;
        try {
            responseMsg = target.path("mock/" + uuid).request()
                    .post(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        } catch (NotFoundException e) {
            responseMsg = e.getResponse().readEntity(JsonNode.class);
        }
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, false);
        Asserts.verifyString(responseMsg, Const.MESSAGE, Const.buildNoSuchSettingsMessage(uuid));
    }

    @Test
    public void putTest() {
        UUID uuid = UUID.randomUUID();
        JsonNode responseMsg;
        try {
            responseMsg = target.path("mock/" + uuid).request()
                    .put(Entity.json(buildTestJson(UUID.randomUUID().toString())), JsonNode.class);
        } catch (NotFoundException e) {
            responseMsg = e.getResponse().readEntity(JsonNode.class);
        }
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, false);
        Asserts.verifyString(responseMsg, Const.MESSAGE, Const.buildNoSuchSettingsMessage(uuid));
    }

    @Test
    public void patchTest() {
        UUID uuid = UUID.randomUUID();
        Response responseMsg = target.path("mock/" + uuid).request().build("PATCH",
                        Entity.json(buildTestJson(UUID.randomUUID().toString())))
                .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true).invoke();
        System.out.println(responseMsg);
        Assert.assertEquals(responseMsg.getStatus(), 404);
    }
}
