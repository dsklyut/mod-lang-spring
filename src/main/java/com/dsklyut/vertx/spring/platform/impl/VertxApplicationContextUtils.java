package com.dsklyut.vertx.spring.platform.impl;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.vertx.java.core.Vertx;
import org.vertx.java.platform.Container;

/**
 * User: dsklyut
 * Date: 3/27/13
 * Time: 12:54 PM
 */
public final class VertxApplicationContextUtils {

    private VertxApplicationContextUtils() {
        // do not initialize me
    }

    /**
     * Register vertx specific singleton beans for use in VertxApplicationContext
     * i.e. vertx, vertxContainer, vertxEventBus, moduleConfig
     *
     * @param bf        the BeanFactory to configure
     * @param vertx     vertx instance
     * @param container vertx container instance
     */
    public static void registerEnvironmentBeans(ConfigurableListableBeanFactory bf, Vertx vertx, Container container) {

        if (vertx != null && !bf.containsBean(VertxApplicationContext.VERTX_BEAN_NAME)) {
            bf.registerSingleton(VertxApplicationContext.VERTX_BEAN_NAME, vertx);
        }

        if (container != null && !bf.containsBean(VertxApplicationContext.VERTX_CONTAINER_BEAN_NAME)) {
            bf.registerSingleton(VertxApplicationContext.VERTX_CONTAINER_BEAN_NAME, container);
        }

        if (vertx != null && vertx.eventBus() != null && !bf.containsBean(VertxApplicationContext.VERTX_EVENT_BUS_BEAN_NAME)) {
            bf.registerSingleton(VertxApplicationContext.VERTX_EVENT_BUS_BEAN_NAME, vertx.eventBus());
        }

        if (container != null && container.config() != null && !bf.containsBean(VertxApplicationContext.VERTX_MODULE_CONFIG_BEAN_NAME)) {
            bf.registerSingleton(VertxApplicationContext.VERTX_MODULE_CONFIG_BEAN_NAME, container.config());
        }
    }
}
