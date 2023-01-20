package com.github.danisimov.mockochino;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.danisimov.mockochino.base.Asserts;
import com.github.danisimov.mockochino.base.ServerTestBase;
import com.github.danisimov.mockochino.service.Const;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * Created by danisimov on 8/6/19
 */
public class DataHandlerWithNotInitializedSettingsTest extends ServerTestBase {

    /**
     * Data tests
     */
    @Test
    public void getRequestByUUIDWithNotInitializedSettingsTest() {
        JsonNode responseMsg = target.path("request/" + UUID.randomUUID()).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyArray(responseMsg, Const.DATA, 0);

        Assert.assertEquals(countStorage(), 0);
    }

    @Test
    public void countByUUIDWithNotInitializedSettingsTest() {
        JsonNode responseMsg = target.path("request/count/" + UUID.randomUUID()).request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
        Asserts.verifyInteger(responseMsg, Const.AMOUNT, 0);

        Assert.assertEquals(countStorage(), 0);
    }
}
