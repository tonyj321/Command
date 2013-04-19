package org.lsst.ccs.command.dictionary;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.annotations.Parameter;

/**
 * Encapsulate the information from a single command method and associated
 * annotations and parameters.
 *
 * @author turri
 */
public class CommandDefinition implements Serializable {

    private final String methodName;
    private final String description;
    private final String abbreviation;
    private final ParameterDefinition[] params;
    private final String targetName;
    private final Command.CommandType type;
    private final String name;

    CommandDefinition(Class target, Method method, Command annotation) {
        this.methodName = method.toGenericString();
        this.targetName = target.getName();
        this.description = annotation.description();
        this.abbreviation = annotation.abbrev();
        this.type = annotation.type();
        this.name = annotation.name().isEmpty() ? method.getName() : annotation.name();

        Class[] types = method.getParameterTypes();
        Annotation[][] parAnnotations = method.getParameterAnnotations();

        params = new ParameterDefinition[types.length];

        for (int i = 0; i < types.length; i++) {

            String parName = "par" + i;
            String parDescription = "";
            for (Annotation a : parAnnotations[i]) {
                if (a instanceof Parameter) {
                    Parameter paramAnnotation = (Parameter) a;
                    parName = paramAnnotation.name().isEmpty() ? parName : paramAnnotation.name();
                    parDescription = paramAnnotation.description();
                    break;
                }
            }
            params[i] = new ParameterDefinition(parName, types[i], parDescription, i);
        }


    }

    public String getMethodName() {
        return methodName;
    }

    public String getDescription() {
        return description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public ParameterDefinition[] getParams() {
        return params;
    }

    public String getTargetName() {
        return targetName;
    }

    public Command.CommandType getType() {
        return type;
    }

    public String getCommandName() {
        return name;
    }

    @Override
    public String toString() {
        String out = "Method " + methodName + " with ";
        if (params.length == 0) {
            out += "no parameters\n";
        } else {
            out += params.length + " parameters: \n";
            for (int i = 0; i < params.length; i++) {
                out += params[i].getName() + " " + params[i].getType() + " " + params[i].getPosition() + " " + params[i].getDescription() + "\n";
            }
        }
        return out;
    }

    boolean isVarArgs() {
        // FIXME: Need to implement
        return false;
    }
}
