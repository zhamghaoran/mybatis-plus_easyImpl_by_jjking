package org.zhr.annotation;

import lombok.Value;

import java.lang.annotation.*;

/**
 * @author 20179
 */
@Documented
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String value();
}
