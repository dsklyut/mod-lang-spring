package com.dsklyut.vertx.spring.support;

import com.dsklyut.vertx.spring.ContainerAware;
import com.dsklyut.vertx.spring.VertxAware;
import org.vertx.java.core.Future;
import org.vertx.java.platform.Verticle;

/**
 * Decorator class to wrap standard java classes in a verticle.
 * <p/>
 * todo: not finished yet
 * <p/>
 * User: dsklyut
 * Date: 3/27/13
 * Time: 3:23 PM
 */
public abstract class VerticleAdapter extends Verticle implements ContainerAware, VertxAware {

    @Override
    public final void start() {
        super.start();
        onStart();
    }

    @Override
    public final void start(Future<Void> startedResult) {
        try {
            onStart();
        } catch (Exception ex) {
            startedResult.setFailure(ex);
        }
        if (!startedResult.failed()) {
            startedResult.setResult(null);
        }
    }

    @Override
    public final void stop() {
        onStop();
    }

    protected abstract void onStart();

    protected void onStop() {
        // do nothing - give subclasses and extension point.
    }
}
