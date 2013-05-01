package org.lsst.ccs.command.dictionary;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.cliche.CLIException;
import org.lsst.ccs.command.cliche.InputConversionEngine;
import org.lsst.ccs.command.cliche.TokenException;

/**
 * Takes a single object and builds a command set from its annotated methods.
 * The command set consists of two parts, the CommandDictionary, which is
 * serializable and the command invoked, which contains references to the object
 * and methods and is not serializable.
 *
 * @author tonyj
 */
public class CommandSetBuilder {

    public CommandSet buildCommandSet(Object object) {
        Class targetClass = object.getClass();
        CommandDictionaryImplementation dict = new CommandDictionaryImplementation();
        CommandSetImplementation result = new CommandSetImplementation(dict, object);
        for (Method method : targetClass.getMethods()) {
            Command annotation = method.getAnnotation(Command.class);
            if (annotation != null) {
                dict.add(new CommandDefinition(method, annotation));
                result.add(method);
            }
        }
        return result;
    }

    private static class CommandSetImplementation implements CommandSet {

        private final CommandDictionaryImplementation dict;
        private final Object target;
        private final List<Method> methods = new ArrayList<>();

        private CommandSetImplementation(CommandDictionaryImplementation dict, Object target) {
            this.dict = dict;
            this.target = target;
        }

        @Override
        public CommandDictionary getCommandDictionary() {
            return dict;
        }

        @Override
        public Object invoke(TokenizedCommand tc) throws CommandInvocationException {
            int index = dict.findCommand(tc);
            if (index >= 0) {
                return invoke(target, methods.get(index), dict.get(index), tc);
            }
            throw new CommandInvocationException("No handler found for command %s with %d arguments", tc.getCommand(), tc.getArgumentCount());
        }

        private void add(Method method) {
            methods.add(method);
        }

        private Object invoke(Object target, Method method, CommandDefinition def, TokenizedCommand tc) throws CommandInvocationException {
            try {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] args = new Object[parameterTypes.length];
                boolean varArgs = method.isVarArgs();
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (varArgs && i == parameterTypes.length - 1) {
                        Class varClass = parameterTypes[i];
                        Class elemClass = varClass.getComponentType();
                        Object theArray = Array.newInstance(elemClass, tc.getArgumentCount() - args.length + 1);
                        for (int j = 0; j < Array.getLength(theArray); j++) {
                            Array.set(theArray, j, InputConversionEngine.convertArgToElementaryType(tc.getArgument(i+j), elemClass));
                        }
                        args[i] = theArray;
                    } else {
                        args[i] = InputConversionEngine.convertArgToElementaryType(tc.getArgument(i), parameterTypes[i]);
                    }
                }
                return method.invoke(target, args);
            } catch (CLIException | IllegalAccessException | IllegalArgumentException ex) {
                throw new CommandSet.CommandInvocationException("Error invoking command", ex);
            } catch (InvocationTargetException ex) {
                throw new CommandSet.CommandInvocationException(ex);
            }
        }
    }

    private static class CommandDictionaryImplementation extends ArrayList<CommandDefinition> implements CommandDictionary {

        @Override
        public boolean containsCommand(TokenizedCommand tc) {
            for (CommandDefinition def : this) {
                if (def.getCommandName().equals(tc.getCommand())
                        && (tc.getArgumentCount() == def.getParams().length
                        || (tc.getArgumentCount() > def.getParams().length && def.isVarArgs()))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int findCommand(TokenizedCommand tc) {
            int index = 0;
            for (CommandDefinition def : this) {
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
