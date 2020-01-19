package rest.base;

import org.glassfish.grizzly.http.server.HttpServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import rest.service.Main;

/**
 * Created by danisimov on 8/6/19
 */
public class ServerTestBase extends Service {

    private HttpServer server;

    @BeforeClass
    public void setUp() {
        server = Main.startServer();
    }

    @AfterClass
    public void tearDown() {
        server.shutdown();
    }
}
