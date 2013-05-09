package org.lsst.ccs.command;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulate the dictionary information about a single parameter.
 * If the parameter is an Enum, we capture the list of legal values (we
 * do not want to require that the Enum class itself be available in a remote 
 * client).
 * @author turri
 */
public class DictionaryParameter implements Serializable {
    
    private String name;
    private String description;
    private String type;
    private String[] values;

    
    DictionaryParameter(String name, Class type, String description) {
        this.name = name;
        this.type = type.getSimpleName();
        if (type.isEnum()) {
            Enum[] enums = ((Class<? extends Enum>) type).getEnumConstants();
            values = new String[enums.length];
            for (int i=0; i<enums.length; i++) {
                values[i] = enums[i].name();
            }
        }
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

    public List<String> getValues() {
        return values == null ? Collections.<String>emptyList() : Collections.unmodifiableList(Arrays.asList(values));
    }
    
    String getDefaultValue() {
        //FIXME: Not implemented yet
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
