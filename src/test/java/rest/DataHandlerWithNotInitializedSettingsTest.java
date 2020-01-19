package rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.Test;
import rest.base.Asserts;
import rest.base.ServerTestBase;
import rest.service.Const;

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
