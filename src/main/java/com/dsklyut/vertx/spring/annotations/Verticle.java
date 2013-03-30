package com.dsklyut.vertx.spring.annotations;

import java.lang.annotation.*;

/**
 * User: dsklyut
 * Date: 3/27/13
 * Time: 3:12 PM
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Verticle {

    boolean worker() default false;

    boolean multiThreaded() default false;
}
