package org.zhr.annotation;

import java.lang.annotation.*;

/**
 * @author 20179
 */
@Documented
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Filed {
    String value();
}
