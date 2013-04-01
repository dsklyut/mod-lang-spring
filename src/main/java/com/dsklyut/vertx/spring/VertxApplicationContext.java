package com.dsklyut.vertx.spring;

import org.springframework.context.ConfigurableApplicationContext;
import org.vertx.java.core.Vertx;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.DeploymentInfo;

/**
 * User: dsklyut
 * Date: 3/27/13
 * Time: 1:11 PM
 */
public interface VertxApplicationContext extends ConfigurableApplicationContext {

  public enum UseParentContext {
    NONE {
      @Override
      public String getKey(DeploymentInfo deploymentInfo) {
        return null;
      }
    }, PARENT {
      @Override
      public String getKey(DeploymentInfo deploymentInfo) {
        return deploymentInfo.getParentDeploymentName();
      }
    }, ROOT {
      @Override
      public String getKey(DeploymentInfo deploymentInfo) {
        return deploymentInfo.getRootDeploymentName();
      }
    };

    public abstract String getKey(DeploymentInfo deploymentInfo);
  }

  String VERTX_BEAN_NAME = "vertx".intern();
  String VERTX_CONTAINER_BEAN_NAME = "vertxContainer".intern();
  String VERTX_EVENT_BUS_BEAN_NAME = "vertxEventBus".intern();
  String VERTX_MODULE_CONFIG_BEAN_NAME = "vertxConfig".intern();


  /**
   * Set the vertx instance.
   * <p>Does not cause an initialization of the context: refresh needs to be
   * called after the setting of all configuration properties.
   *
   * @see #refresh()
   */
  void setVertx(Vertx vertx);

  /**
   * Set the Container for this vertx application context.
   *
   * @see #refresh()
   */
  void setContainer(Container container);

  /**
   * Return the vertx instance for this vertx application context
   */
  Vertx getVertx();

  /**
   * Return the container instance for this vertx application context
   */
  Container getContainer();

  /**
   * Set the namespace for this vertx application context,
   * to be used for building a default context config location.
   * The root vertx application context does not have a namespace.
   */
  void setNamespace(String namespace);

  /**
   * Return the namespace for this vertx application context, if any.
   */
  String getNamespace();

  /**
   * Set the config locations for this vertx application context in init-param style,
   * i.e. with distinct locations separated by commas, semicolons or whitespace.
   * <p>If not set, the implementation is supposed to use a default for the
   * given namespace or the root web application context, as appropriate.
   */
  void setConfigLocation(String configLocation);

  /**
   * Set the config locations for this vertx application context.
   * <p>If not set, the implementation is supposed to use a default for the
   * given namespace or the root vertx application context, as appropriate.
   */
  void setConfigLocations(String[] configLocations);

  /**
   * Return the config locations for this vertx application context,
   * or {@code null} if none specified.
   */
  String[] getConfigLocations();
}
