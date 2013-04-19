package org.lsst.ccs.command.dictionary;

import java.lang.reflect.Method;
import org.lsst.ccs.command.annotations.Command;

/**
 *
 * @author tonyj
 */
public class DictionaryBuilder {
    /**
     * Create a serializable command dictionary for a single class.
     * This class currently assumes that all commands must have annotations, 
     * although this is not strictly necessary.
     */
    public SerializableCommandDictionary createCommandDictionary(Class targetClass) {
        SerializableCommandDictionary result = new SerializableCommandDictionary();
        for(Method method : targetClass.getMethods()) {
            Command annotation = method.getAnnotation(Command.class);
            if (annotation != null) {
                result.add(new CommandDefinition(targetClass,method,annotation));
            }
        }
        return result;
    }
}
