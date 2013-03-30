package com.dsklyut.vertx.spring;

import org.vertx.java.core.eventbus.EventBus;

/**
 * User: dsklyut
 * Date: 3/27/13
 * Time: 12:50 PM
 */
public interface EventBusAware {

    void setEventBus(EventBus eventBus);
}
