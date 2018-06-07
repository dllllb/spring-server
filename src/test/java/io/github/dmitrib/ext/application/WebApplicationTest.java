package io.github.dmitrib.ext.application;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class TestWebApplication extends WebApplication {
    public LocalConnector connector = new LocalConnector();
    
    @Override
    protected Resource getRootResource() {
        return Resource.newClassPathResource("/io/github/dmitrib/ext/application");
    }

    @Override
    protected Connector newConnector() {
        return connector;
    }
}

public class WebApplicationTest {
    @Test
    public void testSimpleServlet() throws Exception {
        final TestWebApplication app = new TestWebApplication();
        app.start();

        HttpTester request = new HttpTester();
        request.setMethod("GET");
        request.setHeader("Host","tester");
        request.setURI("/");
        request.setVersion("HTTP/1.1");

        HttpTester response = new HttpTester();
        response.parse(app.connector.getResponses(request.generate()));

        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getReason());
        assertTrue(response.getContent().contains("Hello"));

        app.stop();
    }
}
