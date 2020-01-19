package rest.base;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.UUID;

/**
 * Created by danisimov on 8/6/19
 */
public class UUIDTestBase extends ServerTestBase {

    protected UUID uuid;

    @BeforeMethod
    public void init() {
        uuid = initSettings();
    }

    @AfterMethod
    public void clean() {
        Assert.assertTrue(deleteStorage());
        Assert.assertTrue(deleteSettings());
    }
}
