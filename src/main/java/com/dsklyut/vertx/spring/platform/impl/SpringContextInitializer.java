package com.dsklyut.vertx.spring.platform.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.vertx.java.core.VoidResult;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: dsklyut
 * Date: 3/26/13
 * Time: 3:40 PM
 */
// todo: handle parent context - i.e. module deploying module and submodule using spring with parent delegation
class SpringContextInitializer extends Verticle {

    public static final String VERTX_CONTEXT_CLASS_CONFIG_PARAM = "contextClass";
    public static final String CONTEXT_INITIALIZER_CLASSES_PARAM = "contextInitializerClasses";

    private VertxApplicationContext context;

    /**
     * this could be xml files or classes...  (in the future - for now only xml)
     */
    private final String[] configLocations;

    public SpringContextInitializer(String[] configLocations) {
        this.configLocations = configLocations;
    }

    @Override
    public void start() throws Exception {
        onStart();
    }

    private void onStart() throws Exception {
        // todo: do we need a way to specify appContext class?
        // todo: maybe get it from config "contextClass"
        final JsonObject config = getContainer().getConfig();

        final Logger logger = container.getLogger();

        logger.info("Initializing Spring VertxApplicationContext");
        if (logger.isInfoEnabled()) {
            logger.info("VertxApplicationContext: initialization started");
        }
        long startTime = System.currentTimeMillis();

        if (this.context == null) {
            this.context = createContext(config);
        }
        configureAndRefresh(context, config);


        if (logger.isInfoEnabled()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("VertxApplicationContext: initialization completed in " + elapsedTime + " ms");
        }
    }

    @Override
    public void start(VoidResult startedResult) throws Exception {
        try {
            onStart();
        } catch (Exception ex) {
            startedResult.setFailure(ex);
        }
        if (!startedResult.failed()) {
            startedResult.setResult();
        }
    }

    @Override
    public void stop() throws Exception {
        if (context != null) {
            context.close();
        }
    }


    //===========  internal
    protected VertxApplicationContext createContext(JsonObject config) {
        Class<?> contextClass = determineContextClass(config);
        if (!VertxApplicationContext.class.isAssignableFrom(contextClass)) {
            throw new ApplicationContextException("Custom context class [" + contextClass.getName() +
                    "] is not of type [" + VertxApplicationContext.class.getName() + "]");
        }
        return (VertxApplicationContext) BeanUtils.instantiateClass(contextClass);
    }

    protected Class<?> determineContextClass(JsonObject config) {
        String contextClassName = config.getString(VERTX_CONTEXT_CLASS_CONFIG_PARAM);
        if (contextClassName != null) {
            try {
                return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
            } catch (ClassNotFoundException ex) {
                throw new ApplicationContextException(
                        "Failed to load custom context class [" + contextClassName + "]", ex);
            }
        } else {
            return XmlVertxApplicationContext.class;
        }
    }

    protected void configureAndRefresh(VertxApplicationContext context, JsonObject config) {
        // need someway to set app context id and handle parent delegation.
        // from sharedData for now?

        context.setContainer(this.container);
        context.setVertx(this.vertx);
        context.setConfigLocations(configLocations);
        // can also add something like ApplicationContextInitializer - from config.
        customizeContext(config, context);
        context.refresh();
    }

    /**
     * Customize the {@link VertxApplicationContext} created by this
     * ContextLoader after config locations have been supplied to the context
     * but before the context is <em>refreshed</em>.
     * <p>The default implementation {@linkplain #determineContextInitializerClasses(JsonObject)
     * determines} what (if any) context initializer classes have been specified through
     * {@linkplain #CONTEXT_INITIALIZER_CLASSES_PARAM context init parameters} and
     * {@linkplain org.springframework.context.ApplicationContextInitializer#initialize invokes each} with the
     * given web application context.
     * <p>Any {@code ApplicationContextInitializers} implementing
     * {@link org.springframework.core.Ordered Ordered} or marked with @{@link
     * org.springframework.core.annotation.Order Order} will be sorted appropriately.
     *
     * @param config             the current config object
     * @param applicationContext the newly created application context
     * @see #CONTEXT_INITIALIZER_CLASSES_PARAM
     * @see org.springframework.context.ApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)
     */
    protected void customizeContext(JsonObject config, VertxApplicationContext applicationContext) {
        List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> initializerClasses =
                determineContextInitializerClasses(config);

        if (initializerClasses.size() == 0) {
            // no ApplicationContextInitializers have been declared -> nothing to do
            return;
        }

        Class<?> contextClass = applicationContext.getClass();
        ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>> initializerInstances =
                new ArrayList<>();

        for (Class<ApplicationContextInitializer<ConfigurableApplicationContext>> initializerClass : initializerClasses) {
            Class<?> initializerContextClass =
                    GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
            Assert.isAssignable(initializerContextClass, contextClass, String.format(
                    "Could not add context initializer [%s] as its generic parameter [%s] " +
                            "is not assignable from the type of application context used by this " +
                            "context loader [%s]: ", initializerClass.getName(), initializerContextClass.getName(),
                    contextClass.getName()));
            initializerInstances.add(BeanUtils.instantiateClass(initializerClass));
        }

        Collections.sort(initializerInstances, new AnnotationAwareOrderComparator());
        for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : initializerInstances) {
            initializer.initialize(applicationContext);
        }
    }

    /**
     * Return the {@link ApplicationContextInitializer} implementation classes to use
     * if any have been specified by {@link #CONTEXT_INITIALIZER_CLASSES_PARAM}.
     *
     * @param config current config object
     * @see #CONTEXT_INITIALIZER_CLASSES_PARAM
     */
    @SuppressWarnings("unchecked")
    protected List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> determineContextInitializerClasses(JsonObject config) {
        List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> classes =
                new ArrayList<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>>();
        Object field = config.getField(CONTEXT_INITIALIZER_CLASSES_PARAM);
        List<String> classNames = new ArrayList<>();
        if (field != null) {
            if (field instanceof String) {
                classNames = Arrays.asList(StringUtils.tokenizeToStringArray((String) field, ","));
            } else if (field instanceof JsonArray) {
                for (Object c : ((JsonArray) field)) {
                    classNames.add(c.toString());
                }
            }
        }

        for (String className : classNames) {
            try {
                Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
                Assert.isAssignable(ApplicationContextInitializer.class, clazz,
                        "class [" + className + "] must implement ApplicationContextInitializer");
                classes.add((Class<ApplicationContextInitializer<ConfigurableApplicationContext>>) clazz);
            } catch (ClassNotFoundException ex) {
                throw new ApplicationContextException(
                        "Failed to load context initializer class [" + className + "]", ex);
            }
        }

        return classes;
    }

}
