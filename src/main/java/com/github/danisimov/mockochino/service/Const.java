package com.github.danisimov.mockochino.service;

import java.util.UUID;

public class Const {

    public static final String AMOUNT = "amount";
    public static final String CODE = "code";
    public static final String DATA = "data";
    public static final String DELAY = "delay";
    public static final String ECHO = "echo";
    public static final String MESSAGE = "message";
    public static final String RESPONSE = "response";
    public static final String RESULT = "result";
    public static final String TOKEN = "token";
    public static final String UUID = "uuid";

    public static final String INSERTING_DATA_ERROR = "Inserting data error.";
    public static final String UPDATING_DATA_ERROR = "Updating data error.";

    private Const() {
    }

    public static String buildNoSuchSettingsMessage(UUID uuid) {
        return "No settings with uuid - '" + uuid + "' found.";
    }
}
