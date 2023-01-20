package com.github.danisimov.mockochino.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.danisimov.mockochino.service.Const;
import com.github.danisimov.mockochino.service.Main;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import java.util.UUID;

class Service {

    protected final ObjectMapper objMapper;
    protected final WebTarget target;

    Service() {
        objMapper = new ObjectMapper();
        Client c = ClientBuilder.newClient();
        // set request route
        target = c.target(Main.BASE_URI);
    }

    protected JsonNode buildTestJson(String requestId) {
        return new ObjectMapper().createObjectNode()
                .put("method", "send")
                .put("id", requestId)
                .put("jsonrpc", "2.0")
                .set("params", objMapper.createObjectNode()
                        .set("data", objMapper.createArrayNode()
                                .add(objMapper.createObjectNode()
                                        .put("data_id", UUID.randomUUID().toString()))
                                .add(objMapper.createObjectNode()
                                        .put("data_id", UUID.randomUUID().toString()))));
    }

    protected UUID initSettings() {
        JsonNode responseMsg = target.path("settings/init").request().post(null, JsonNode.class);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyString(responseMsg, Const.UUID);
        return UUID.fromString(responseMsg.get(Const.UUID).asText());
    }

    ArrayNode getSettings() {
        JsonNode responseMsg = target.path("settings").request().get(JsonNode.class);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA);
        return (ArrayNode) responseMsg.get(Const.DATA);
    }

    boolean deleteSettings() {
        ArrayNode settings = getSettings();
        settings.forEach(v -> deleteSettingsByUUID(UUID.fromString(v.get(Const.UUID).asText())));
        return getSettings().size() == 0;
    }

    void deleteSettingsByUUID(UUID uuid) {
        JsonNode responseMsg = target.path("settings/" + uuid).request().delete(JsonNode.class);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
    }

    protected int countStorage() {
        JsonNode responseMsg = target.path("request/count").request().get(JsonNode.class);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyInteger(responseMsg, Const.AMOUNT);
        return responseMsg.get(Const.AMOUNT).asInt();
    }

    protected int countStorageByUUID(UUID uuid) {
        JsonNode responseMsg = target.path("request/count/" + uuid).request().get(JsonNode.class);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyInteger(responseMsg, Const.AMOUNT);
        return responseMsg.get(Const.AMOUNT).asInt();
    }

    boolean deleteStorage() {
        JsonNode responseMsg = target.path("request").request().delete(JsonNode.class);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        return countStorage() == 0;
    }
}
