package com.dsklyut.vertx.spring.platform.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.impl.VertxInternal;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.Verticle;
import org.vertx.java.platform.VerticleFactory;
import org.vertx.java.platform.impl.DefaultContainer;

import static org.springframework.util.Assert.notNull;

/**
 * User: dsklyut
 * Date: 3/26/13
 * Time: 3:37 PM
 */
@SuppressWarnings("unused")
public class SpringVerticleFactory implements VerticleFactory {

    private final static Log logger = LogFactory.getLog(SpringVerticleFactory.class);

    //todo: do we need this stuff?
    private Vertx vertx;
    private Container container;
    private ClassLoader moduleClassLoader;

    @Override
    public void init(Vertx vertx, Container container, ClassLoader cl) {
        logger.info("init of SpringVerticleFactory");
        notNull(vertx, "vertx is null");
        notNull(container, "container is null");
        notNull(cl, "cl is null");
        this.vertx = vertx;
        this.container = container;
        this.moduleClassLoader = cl;
    }

    @Override
    public Verticle createVerticle(String main) throws Exception {
        logger.info("Spring verticle factory - create for '" + main + "'");
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
        System.out.println("close of SpringVerticleFactory");
        // do nothing as there is nothing we can do
        logger.info("Close called on SpringVerticleFactory");
    }
}
