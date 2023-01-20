package com.github.danisimov.mockochino;

import com.fasterxml.jackson.databind.JsonNode;
import org.testng.annotations.Test;
import com.github.danisimov.mockochino.base.Asserts;
import com.github.danisimov.mockochino.base.ServerTestBase;
import com.github.danisimov.mockochino.service.Const;

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
