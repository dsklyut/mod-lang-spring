package com.dsklyut.vertx.spring.support;

import com.dsklyut.vertx.spring.ContainerAware;
import com.dsklyut.vertx.spring.VertxAware;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VoidResult;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.Verticle;

/**
 * Decorator class to wrap standard java classes in a verticle.
 *
 * todo: not finished yet
 * <p/>
 * User: dsklyut
 * Date: 3/27/13
 * Time: 3:23 PM
 */
public abstract class VerticleAdapter extends Verticle implements ContainerAware, VertxAware {

    @Override
    public final void start() throws Exception {
        super.start();
        onStart();
    }

    @Override
    public final void start(VoidResult startedResult) throws Exception {
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
    public final void stop() throws Exception {
        onStop();
    }

    protected abstract void onStart() throws Exception;

    protected void onStop() throws Exception {
        // do nothing - give subclasses and extension point.
    }
}
