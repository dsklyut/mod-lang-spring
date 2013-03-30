package com.dsklyut.vertx.spring.platform.impl;

import com.dsklyut.vertx.spring.ConfigAware;
import com.dsklyut.vertx.spring.ContainerAware;
import com.dsklyut.vertx.spring.EventBusAware;
import com.dsklyut.vertx.spring.VertxAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.vertx.java.core.impl.VertxInternal;
import org.vertx.java.platform.Container;

import java.io.IOException;

/**
 * XML Configuration based VertxApplicationContext
 * <p/>
 * User: dsklyut
 * Date: 3/26/13
 * Time: 4:31 PM
 */
public class XmlVertxApplicationContext extends AbstractVertxApplicationContext {

    public XmlVertxApplicationContext(VertxInternal vertxInternal, Container container) {
        super(vertxInternal, container);
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

    /**
     * Loads the bean definitions via an XmlBeanDefinitionReader.
     *
     * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
     * @see #loadBeanDefinitions
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        // Create a new XmlBeanDefinitionReader for the given BeanFactory.
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

        // Configure the bean definition reader with this context's
        // resource loading environment.
        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(this);
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

        // Allow a subclass to provide custom initialization of the reader,
        // then proceed with actually loading the bean definitions.
        initBeanDefinitionReader(beanDefinitionReader);
        loadBeanDefinitions(beanDefinitionReader);
    }

    /**
     * Initialize the bean definition reader used for loading the bean
     * definitions of this context. Default implementation is empty.
     * <p>Can be overridden in subclasses, e.g. for turning off XML validation
     * or using a different XmlBeanDefinitionParser implementation.
     *
     * @param beanDefinitionReader the bean definition reader used by this context
     * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader#setValidationMode
     * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader#setDocumentReaderClass
     */
    protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
    }

    /**
     * Load the bean definitions with the given XmlBeanDefinitionReader.
     * <p>The lifecycle of the bean factory is handled by the refreshBeanFactory method;
     * therefore this method is just supposed to load and/or register bean definitions.
     * <p>Delegates to a ResourcePatternResolver for resolving location patterns
     * into Resource instances.
     *
     * @throws java.io.IOException if the required XML document isn't found
     * @see #refreshBeanFactory
     * @see #getConfigLocations
     * @see #getResources
     * @see #getResourcePatternResolver
     */
    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws IOException {
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            for (String configLocation : configLocations) {
                reader.loadBeanDefinitions(configLocation);
            }
        }
    }
}
