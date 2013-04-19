package org.lsst.ccs.command.dictionary;

import java.io.Serializable;

/**
 *
 * @author turri
 */
public class ParameterDefinition implements Serializable {
    
    private String name, description;
    private Class type;
    private int position;

    
    ParameterDefinition(String name, Class type, String description, int position) {
        this.name = name;
        this.type = type;
        this.position = position;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    String getDefaultValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
