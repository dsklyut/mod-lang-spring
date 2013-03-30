package com.dsklyut.vertx.spring.platform.impl;

import com.dsklyut.vertx.spring.ConfigAware;
import com.dsklyut.vertx.spring.ContainerAware;
import com.dsklyut.vertx.spring.EventBusAware;
import com.dsklyut.vertx.spring.VertxAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.vertx.java.core.Vertx;
import org.vertx.java.platform.Container;

import static org.springframework.util.Assert.notNull;

/**
 * User: dsklyut
 * Date: 3/27/13
 * Time: 12:33 PM
 */
public class VertxAwareProcessor implements BeanPostProcessor {
    private final Vertx vertx;
    private final Container container;

    public VertxAwareProcessor(Vertx vertx, Container container) {
        notNull(vertx);
        notNull(container);
        this.vertx = vertx;
        this.container = container;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof VertxAware) {
            ((VertxAware) bean).setVertx(this.vertx);
        }
        if (bean instanceof ContainerAware) {
            ((ContainerAware) bean).setContainer(this.container);
        }
        if (bean instanceof ConfigAware && this.container.getConfig() != null) {
            ((ConfigAware) bean).setConfig(this.container.getConfig());
        }
        if (bean instanceof EventBusAware) {
            ((EventBusAware) bean).setEventBus(this.vertx.eventBus());
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
