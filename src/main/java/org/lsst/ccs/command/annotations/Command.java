package org.lsst.ccs.command.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for commands. Allows to specify the name of a command, otherwise
 * method's name is used.
 *
 * @author turri
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Command {

    public static enum CommandType {

        QUERY,
        ACTION,
        CONFIGURATION,
        ABORT
    }
    // Some pre-defined levels, this list is not necessarily exhaustive, which is
    // why we do not use an enumeration.
    public static final int NORMAL = 0;
    public static final int ENGINEERING1 = 1;
    public static final int ENGINEERING2 = 2;
    public static final int ENGINEERING3 = 3;

    /**
     * If not null it will replace the method's name as the command name.
     *
     * @return "" or null if default name is used, user-specified name
     * otherwise.
     */
    String name() default ""; // if "" then Null is assumed.

    /**
     * Specify the description of the command. Default description (if this
     * property is not set) says "methodName(Arg1Type, Arg2Type,...) :
     * ReturnType".
     *
     * @return command's description or "" if not set.
     */
    String description() default "";

    /**
     * Specify the shortcut name for the command. If not set, if the name
     * attribute is not set as well, the Shell takes the first letter of each
     * word (void selectUser() --- select-user --- su).
     *
     * @return command's abbreviation(s) or "" if not set.
     */
    String alias() default "";

    /**
     * The CommandType of the Command
     *
     * @return The command type.
     */
    CommandType type() default CommandType.ACTION;

    /**
     * Specify the level of the command. Only locks at the given level, or
     * higher will have access to the command.
     *
     * @return the level of the Command. The default is zero.
     */
    int level() default NORMAL;
}
