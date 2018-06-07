package io.github.dmitrib.ext.application;

import org.eclipse.jetty.util.resource.Resource;

/**
 * @author Dmitri Babaev
 */
public class SimpleWebApplicaion extends WebApplication {
    @Override
    protected Resource getRootResource() {
        return Resource.newClassPathResource("/org/ext/application");
    }

    public static void main(String[] args) {
        new SimpleWebApplicaion().doMain();
    }
}
