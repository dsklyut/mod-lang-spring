package com.dsklyut.vertx.spring.platform.impl;


import com.dsklyut.vertx.spring.test.LifecycleTestingClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.PlatformLocator;
import org.vertx.java.platform.PlatformManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * User: dsklyut
 * Date: 4/1/13
 * Time: 10:15 AM
 */
public class TestXmlVertxApplicationContext {

    @Test
    public void validateLoadingOfSimpleContext() {
        JsonObject config = new JsonObject();
        PlatformManager pm = PlatformLocator.factory.createPlatformManager();
        Vertx vertx = pm.vertx();
        // there is no cntr that we can safely use (package private) so just mock for this test
        Container mockContainer = Mockito.mock(Container.class);
        when(mockContainer.config()).thenReturn(config);

        VertxApplicationContext cntx = new XmlVertxApplicationContext();
        cntx.setVertx(vertx);
        cntx.setContainer(mockContainer);
        cntx.setConfigLocation("contexts/testLifecycleInterfaces.xml");

        try {
            cntx.refresh();

            LifecycleTestingClass underTest = cntx.getBean("support", LifecycleTestingClass.class);
            assertNotNull(underTest);
            assertNotNull(underTest.getVertx());
            assertNotNull(underTest.getConfig());
            assertNotNull(underTest.getConfig());
            assertNotNull(underTest.getEb());

            assertEquals(config, underTest.getConfig());
        } catch (Exception ex) {
            fail(ex.getMessage());
        } finally {
            if (cntx.isActive()) {
                cntx.close();
            }
        }
    }
}
