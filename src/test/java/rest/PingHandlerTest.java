package rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.testng.annotations.Test;
import rest.base.Asserts;
import rest.base.ServerTestBase;
import rest.service.Const;

/**
 * Created by danisimov on 8/6/19
 */
public class PingHandlerTest extends ServerTestBase {

    @Test
    public void pingTest() {
        JsonNode responseMsg = target.request().get(JsonNode.class);
        System.out.println(responseMsg);
        Asserts.verifyBoolean(responseMsg, Const.RESULT, true);
    }
}
