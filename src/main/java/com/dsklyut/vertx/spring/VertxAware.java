package com.dsklyut.vertx.spring;

import org.vertx.java.core.Vertx;

/**
 * User: dsklyut
 * Date: 3/27/13
 * Time: 12:31 PM
 */
public interface VertxAware {

  void setVertx(Vertx vertx);
}
