package org.lsst.ccs.command;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.annotations.Parameter;

/**
 * Encapsulate the dictionary information for a single command and parameters.
 * This class is serializable for use in client-server applications, so does not
 * contain maintain any references to Class or Method objects which may not be
 * available in a remote client.
 * @author turri
 */
public class DictionaryCommand implements Serializable {

    private final String description;
    private final String abbreviation;
    private final DictionaryParameter[] params;
    private final Command.CommandType type;
    private final String name;
    private final boolean hasVarArgs;
    /** 
     * Create a command definition from a method and associated annotation.
     * @param method The method providing the command implementation
     * @param annotation The annotation on the method
     */
    
    DictionaryCommand(Method method, Command annotation) {
        this.description = annotation.description();
        this.abbreviation = annotation.abbrev();
        this.type = annotation.type();
        this.name = annotation.name().isEmpty() ? method.getName() : annotation.name();
        this.hasVarArgs = method.isVarArgs();
        
        Class[] types = method.getParameterTypes();
        Annotation[][] parAnnotations = method.getParameterAnnotations();

        params = new DictionaryParameter[types.length];

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
            params[i] = new DictionaryParameter(parName, types[i], parDescription);
        }


    }

    public String getDescription() {
        return description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public DictionaryParameter[] getParams() {
        return params;
    }

    public Command.CommandType getType() {
        return type;
    }

    public String getCommandName() {
        return name;
    }

    public boolean isVarArgs() {
        return hasVarArgs;
    }
}
