package org.lsst.ccs.command.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for parameters of Command-marked methods. This annotation is of
 * particular usefulness, because Java 5 Reflection doesn't have access to
 * declared parameter names (there's simply no such information stored in
 * classfile).
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Argument {

    /**
     * Optional parameter name. If not set the argument name will be "arg0",
     * "arg1"....
     *
     * @return The name of the argument.
     */
    String name();

    /**
     * One-sentence description of the parameter.
     *
     * @return Short description of the annotated argument.
     */
    String description() default "";

    /**
     * Default value for this Argument.
     *
     * @return the default value for this Argument.
     */
    String defaultValue() default "";
}
