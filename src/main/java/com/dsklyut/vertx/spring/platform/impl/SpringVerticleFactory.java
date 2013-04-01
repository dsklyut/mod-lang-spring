package com.dsklyut.vertx.spring.platform.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.DeploymentInfo;
import org.vertx.java.platform.ExtendedVerticleFactory;
import org.vertx.java.platform.Verticle;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.StringUtils.commaDelimitedListToStringArray;

/**
 * User: dsklyut
 * Date: 3/26/13
 * Time: 3:37 PM
 */
@SuppressWarnings("unused")
public class SpringVerticleFactory implements ExtendedVerticleFactory {

  // oh man I really hate leaving this in there as commons-logging
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
  public Verticle createVerticle(DeploymentInfo dpi) throws Exception {
    // can't use container.getLogger() as it is not initialized yet.  i.e. null
    logger.info("Spring verticle factory - create for '" + dpi.getMain() + "'");
    if (logger.isDebugEnabled()) {
      logger.debug("Spring verticle factory - deploymentInfo: " + dpi);
    }

    // this is ain't enough as we should only create a single AppCntx in case of "instances > 1"
    return new SpringContextInitializer(commaDelimitedListToStringArray(dpi.getMain()));
  }

  @Override
  public Verticle createVerticle(String main) throws Exception {
    throw new UnsupportedOperationException("don't call me - call my extended cousin");
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
