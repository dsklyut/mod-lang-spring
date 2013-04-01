package com.dsklyut.vertx.spring;

import org.vertx.java.platform.Container;

/**
 * User: dsklyut
 * Date: 3/27/13
 * Time: 12:31 PM
 */
public interface ContainerAware {

  void setContainer(Container container);
}
