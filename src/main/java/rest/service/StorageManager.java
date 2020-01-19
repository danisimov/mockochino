package rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Class for storing received requests.
 * Implemented as singleton, as alternative for keeping it in Main.
 */
public class StorageManager {

    private static StorageManager instance;
    private static ObjectMapper objectMapper;

    private StorageManager() {
    }

    public static synchronized StorageManager getInstance() {
        if (instance == null) {
            instance = new StorageManager();
            objectMapper = new ObjectMapper();
        }
        return instance;
    }

    public synchronized boolean add(UUID uuid, String request) {
        return DBClient.insert("INSERT INTO storage VALUES ('" + uuid + "','" + request + "')");
    }

    public synchronized ArrayNode getByUUID(UUID uuid) {
        try (ResultSet rs = DBConnect.getStatement()
                .executeQuery("SELECT request FROM storage WHERE uuid='" + uuid + "'")) {
            ArrayNode arrayNode = objectMapper.createArrayNode();
            while (rs.next()) {
                arrayNode.add(objectMapper.readTree(rs.getString("request")));
            }
            return arrayNode;
        } catch (SQLException | IOException e) {
            Logger.getLogger(SettingsManager.class.getName()).warning(e.getMessage());
            return objectMapper.createArrayNode();
        }
    }

    public synchronized int count() {
        return DBClient.count("SELECT COUNT(*) FROM storage");
    }

    public synchronized int countByUUID(UUID uuid) {
        return DBClient.count("SELECT COUNT(*) FROM storage WHERE uuid='" + uuid + "'");
    }

    public synchronized boolean delete() {
        return DBClient.delete("DELETE FROM storage");
    }

    public synchronized boolean deleteByUUID(UUID uuid) {
        return DBClient.delete("DELETE FROM storage WHERE uuid='" + uuid + "'");
    }
}
