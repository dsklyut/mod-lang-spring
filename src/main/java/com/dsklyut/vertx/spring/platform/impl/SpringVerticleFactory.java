package com.dsklyut.vertx.spring.platform.impl;

import org.springframework.util.StringUtils;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.Verticle;
import org.vertx.java.platform.VerticleFactory;

/**
 * User: dsklyut
 * Date: 3/26/13
 * Time: 3:37 PM
 */
@SuppressWarnings("unused")
public class SpringVerticleFactory implements VerticleFactory {

    //todo: do we need this stuff?
    private Vertx vertx;
    private Container container;
    private ClassLoader moduleClassLoader;

    @Override
    public void init(Vertx vertx, Container container, ClassLoader cl) {
        this.vertx = vertx;
        this.container = container;
        this.moduleClassLoader = cl;
    }

    @Override
    public Verticle createVerticle(String main) throws Exception {
        container.getLogger().info("Spring verticle factory - create for '" + main + "'");
        // account for deployment in this format only for now:
        // resource-prefix:path-to-xml-file
        // todo: handle java-config based deployments.
        return new SpringContextInitializer(StringUtils.commaDelimitedListToStringArray(main));
    }

    @Override
    public void reportException(Logger logger, Throwable t) {
        logger.error("Exception in Spring verticle script", t);
    }

    @Override
    public void close() {
        // do nothing as there is nothing we can do
        container.getLogger().info("Close called on SpringVerticleFactory");
    }
}
