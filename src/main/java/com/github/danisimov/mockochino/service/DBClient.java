package com.github.danisimov.mockochino.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Created by danisimov on 1/15/20
 */
class DBClient {

    private DBClient() {
    }

    static boolean insert(String query) {
        return executeUpdate(query);
    }

    static boolean update(String query) {
        return executeUpdate(query);
    }

    static int count(String query) {
        try (ResultSet rs = DBConnect.getStatement().executeQuery(query)) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            Logger.getLogger(SettingsManager.class.getName()).warning(e.getMessage());
            return 0;
        }
    }

    static boolean delete(String query) {
        return executeUpdate(query);
    }

    private static boolean executeUpdate(String query) {
        try (Statement statement = DBConnect.getStatement()) {
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            Logger.getLogger(SettingsManager.class.getName()).warning(e.getMessage());
            return false;
        }
    }
}
