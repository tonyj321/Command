package org.lsst.ccs.command.dictionary;

import java.io.Serializable;

/**
 * Encapsulate the dictionary information about a single parameter.
 * @author turri
 */
public class ParameterDefinition implements Serializable {
    
    private String name;
    private String description;
    private String type;

    
    ParameterDefinition(String name, Class type, String description) {
        this.name = name;
        this.type = type.getSimpleName();
        this.description = description;
    }
    
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    String getDefaultValue() {
        //FIXME: Not implemented yet
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
