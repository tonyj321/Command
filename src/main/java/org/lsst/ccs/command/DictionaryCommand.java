package org.lsst.ccs.command;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.annotations.Argument;

/**
 * Encapsulate the dictionary information for a single command and parameters.
 * This class is serializable for use in client-server applications, so does not
 * maintain any references to Class or Method objects which may not be
 * available in a remote client.
 * @author turri
 */
public class DictionaryCommand implements Serializable {

    private final String description;
    private final String aliases;
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
        this.aliases = annotation.alias();
        this.type = annotation.type();
        this.name = annotation.name().isEmpty() ? method.getName() : annotation.name();
        this.hasVarArgs = method.isVarArgs();
        
        Class[] types = method.getParameterTypes();
        Annotation[][] parAnnotations = method.getParameterAnnotations();

        params = new DictionaryParameter[types.length];

        for (int i = 0; i < types.length; i++) {

            String parName = "arg" + i;
            String parDescription = "";
            for (Annotation a : parAnnotations[i]) {
                if (a instanceof Argument) {
                    Argument paramAnnotation = (Argument) a;
                    parName = paramAnnotation.name().isEmpty() ? parName : paramAnnotation.name();
                    parDescription = paramAnnotation.description();
                    break;
                }
            }
            Class parameterType = hasVarArgs && i==types.length-1 ? types[i].getComponentType() : types[i];
            params[i] = new DictionaryParameter(parName, parameterType, parDescription);
        }


    }

    public String getDescription() {
        return description;
    }

    public String getAbbreviation() {
        return aliases;
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
