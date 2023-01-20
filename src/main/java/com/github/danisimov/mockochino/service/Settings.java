package com.github.danisimov.mockochino.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

/**
 * Class for storing settings.
 * Implemented as singleton, as alternative for keeping it in Main.
 */
public class Settings {

    private UUID uuid;
    private int delay;
    private int code;
    private String token;
    private JsonNode response;

    Settings() {
    }

    Settings(UUID uuid) {
        setUuid(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public JsonNode getResponse() {
        return response;
    }

    public void setResponse(JsonNode response) {
        this.response = response;
    }

    public Settings setDefaultSettings() {
        setDelay(0);
        setCode(200);
        setToken("");
        setResponse(new ObjectMapper().createObjectNode()
                .put(Const.RESULT, true)
                .put(Const.MESSAGE, Const.ECHO));
        return this;
    }
}
