package com.dsklyut.vertx.spring.platform.impl;

import com.dsklyut.vertx.spring.ConfigAware;
import com.dsklyut.vertx.spring.ContainerAware;
import com.dsklyut.vertx.spring.EventBusAware;
import com.dsklyut.vertx.spring.VertxAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.vertx.java.core.Vertx;
import org.vertx.java.platform.Container;

import static org.springframework.util.Assert.notNull;

/**
 * Base class for Vertx ApplicationContext support.
 * <p/>
 * User: dsklyut
 * Date: 3/27/13
 * Time: 1:07 PM
 */
public abstract class AbstractVertxApplicationContext
        extends AbstractRefreshableConfigApplicationContext implements VertxApplicationContext {


    private Vertx vertx;
    private Container container;
    private String namespace;

    public AbstractVertxApplicationContext() {
        setDisplayName("Root VertxApplicationContext");
    }

    public AbstractVertxApplicationContext(Vertx vertxInternal, Container container) {
        notNull(vertxInternal);
        notNull(container);
        this.vertx = vertxInternal;
        this.container = container;
    }

    @Override
    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Vertx getVertx() {
        return this.vertx;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public String[] getConfigLocations() {
        return super.getConfigLocations();
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new VertxAwareProcessor(getVertx(), getContainer()));
        beanFactory.ignoreDependencyInterface(VertxAware.class);
        beanFactory.ignoreDependencyInterface(ConfigAware.class);
        beanFactory.ignoreDependencyInterface(ContainerAware.class);
        beanFactory.ignoreDependencyInterface(EventBusAware.class);

        VertxApplicationContextUtils.registerEnvironmentBeans(beanFactory, getVertx(), getContainer());
    }
}
