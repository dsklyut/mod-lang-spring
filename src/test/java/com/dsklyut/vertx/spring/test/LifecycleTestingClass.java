package com.dsklyut.vertx.spring.test;

import com.dsklyut.vertx.spring.ConfigAware;
import com.dsklyut.vertx.spring.ContainerAware;
import com.dsklyut.vertx.spring.EventBusAware;
import com.dsklyut.vertx.spring.VertxAware;
import org.springframework.beans.factory.InitializingBean;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

import static org.springframework.util.Assert.notNull;

/**
 * User: dsklyut
 * Date: 4/1/13
 * Time: 10:00 AM
 */
public class LifecycleTestingClass implements VertxAware, ContainerAware, EventBusAware, ConfigAware, InitializingBean {


  private JsonObject config;
  private Container container;
  private EventBus eb;
  private Vertx vertx;


  @Override
  public void setConfig(JsonObject config) {
    this.config = config;
  }

  @Override
  public void setContainer(Container container) {
    this.container = container;
  }

  @Override
  public void setEventBus(EventBus eventBus) {
    this.eb = eventBus;
  }

  @Override
  public void setVertx(Vertx vertx) {
    this.vertx = vertx;
  }

  public JsonObject getConfig() {
    return config;
  }

  public Container getContainer() {
    return container;
  }

  public EventBus getEb() {
    return eb;
  }

  public Vertx getVertx() {
    return vertx;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    notNull(config, "Config is null");
    notNull(container, "Container is null");
    notNull(vertx, "Vertx is null");
    notNull(eb, "EventBus is null");
  }
}
