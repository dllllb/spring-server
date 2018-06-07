package io.github.dmitrib.ext.application;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Embedded server for simple development and debug of a web application inside IDE.<br/>
 * If JSP support is needed Jasper JSP engine and Apache JSTL taglib should be in the application classpath.<br/>
 * WEB-INF folder should be in classpath by default.
 *
 * @author Dmitri Babaev
 */
public class WebApplication {
    private Server server;
    /**
     * This function is designed to be used from the main function<br/>
     * Example:
     * <code>
     * public static void main(String[] args) {
     * new WebApplication().doMain();
     * }
     * </code>
     */
    public void doMain() {
        start();

        try {
            server.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void start() {
        server = new Server();

        server.addConnector(newConnector());

        WebAppContext wac = new WebAppContext();
        wac.setContextPath("/");
        wac.setBaseResource(getRootResource());

        server.setHandler(wac);
        server.setStopAtShutdown(true);

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            if (server != null) {
                server.stop();
                server.join();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Connector newConnector() {
        Connector connector = new SelectChannelConnector();
        connector.setPort(getPort());
        connector.setHost(getHostName());
        return connector;
    }

    protected int getPort() {
        return 8080;
    }

    protected String getHostName() {
        return "localhost";
    }

    /**
     * Using classpath WEB-INF folder by default
     * WARNING: method not working (returns null) for classpath without plain file-system directory
     * (i. e. there is only jar files in classpath and WEB-INF/web.xml is inside jar file)
     */
    protected Resource getRootResource() {
        return Resource.newClassPathResource("/");
    }

    public static void main(String[] args) {
        new WebApplication().doMain();
    }
}
