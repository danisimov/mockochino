package com.github.danisimov.mockochino.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Class for managing settings.
 * Implemented as singleton, as alternative for keeping it in Main.
 */
public class SettingsManager {

    private static SettingsManager instance;
    private static ObjectMapper objectMapper;

    private SettingsManager() {
    }

    public static synchronized SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
            objectMapper = new ObjectMapper();
        }
        return instance;
    }

    public synchronized boolean add(UUID uuid) {
        if (!exists(uuid)) {
            Settings settings = new Settings(uuid).setDefaultSettings();
            return DBClient.insert("INSERT INTO settings VALUES ('" + settings.getUuid()
                    + "'," + settings.getDelay() + "," + settings.getCode()
                    + ",'" + settings.getToken() + "','" + settings.getResponse() + "')");
        } else return false;
    }

    public synchronized ArrayNode get() {
        try (ResultSet rs = DBConnect.getStatement()
                .executeQuery("SELECT * FROM settings")) {
            ArrayNode arrayNode = objectMapper.createArrayNode();
            while (rs.next()) {
                arrayNode.add(objectMapper.createObjectNode()
                        .put(Const.UUID, rs.getString(Const.UUID))
                        .put(Const.DELAY, rs.getInt(Const.DELAY))
                        .put(Const.CODE, rs.getInt(Const.CODE))
                        .put(Const.TOKEN, rs.getString(Const.TOKEN))
                        .set(Const.RESPONSE, objectMapper.readTree(rs.getString(Const.RESPONSE))));
            }
            return arrayNode;
        } catch (SQLException | IOException e) {
            Logger.getLogger(SettingsManager.class.getName()).warning(e.getMessage());
            return objectMapper.createArrayNode();
        }
    }

    public synchronized Settings getByUUID(UUID uuid) {
        try (ResultSet rs = DBConnect.getStatement()
                .executeQuery("SELECT * FROM settings WHERE uuid = '" + uuid + "'")) {
            Settings settings = new Settings();
            settings.setUuid(UUID.fromString(rs.getString(Const.UUID)));
            settings.setDelay(rs.getInt(Const.DELAY));
            settings.setCode(rs.getInt(Const.CODE));
            settings.setToken(rs.getString(Const.TOKEN));
            settings.setResponse(objectMapper.readTree(rs.getString(Const.RESPONSE)));
            return settings;
        } catch (SQLException | IOException e) {
            Logger.getLogger(SettingsManager.class.getName()).warning(e.getMessage());
            return null;
        }
    }

    private synchronized boolean exists(UUID uuid) {
        try (ResultSet rs = DBConnect.getStatement()
                .executeQuery("SELECT uuid FROM settings WHERE uuid = '" + uuid + "'")) {
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            Logger.getLogger(SettingsManager.class.getName()).warning(e.getMessage());
            return false;
        }
    }

    public synchronized boolean update(Settings settings) {
        return DBClient.update("UPDATE settings SET delay = " + settings.getDelay()
                + ", code = " + settings.getCode() + ",token = '" + settings.getToken() + "',response = '"
                + settings.getResponse() + "' where uuid = '" + settings.getUuid() + "'");
    }

    public synchronized boolean deleteByUUID(UUID uuid) {
        return DBClient.delete("DELETE FROM settings WHERE uuid='" + uuid + "'");
    }
}
