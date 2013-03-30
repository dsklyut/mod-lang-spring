package com.dsklyut.vertx.spring;

import org.vertx.java.core.json.JsonObject;

/**
 * User: dsklyut
 * Date: 3/27/13
 * Time: 12:30 PM
 */
public interface ConfigAware {

    void setConfig(JsonObject config);
}
