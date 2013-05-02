package org.lsst.ccs.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import org.lsst.ccs.command.annotations.Command;

/**
 * Builds a command dictionary for a given class using annotations on the
 * methods of the class.
 *
 * @author tonyj
 */
class DictionaryBuilder {

    /**
     * Build a command dictionary for a class using annotations on the methods
     * of the class.
     * @param targetClass The class for which the dictionary should be built
     * @return The dictionary created.
     */
    Dictionary build(Class targetClass) {
        CommandDictionaryImplementation dict = new CommandDictionaryImplementation();
        for (Method method : targetClass.getMethods()) {
            Command annotation = method.getAnnotation(Command.class);
            if (annotation != null) {
                dict.add(new DictionaryCommand(method, annotation));
            }
        }
        // FIXME: Should this throw an exception if no annotations are found, or just and 
        // empty dictionary as we do now?
        return dict;
    }

    private static class CommandDictionaryImplementation extends ArrayList<DictionaryCommand> implements Dictionary {

        @Override
        public boolean containsCommand(TokenizedCommand tc) {
            return findCommand(tc) != -1;
        }

        @Override
        public int findCommand(TokenizedCommand tc) {
            int index = 0;
            for (DictionaryCommand def : this) {
                if (def.getCommandName().equals(tc.getCommand())
                        && (tc.getArgumentCount() == def.getParams().length
                        || (tc.getArgumentCount() > def.getParams().length && def.isVarArgs()))) {
                    return index;
                }
                index++;
            }
            return -1;
        }
    }
}
