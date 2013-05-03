package org.lsst.ccs.command;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.lsst.ccs.command.annotations.Command;

/**
 * Takes a single object and builds a command set from its annotated methods.
 * The command set consists of two parts, the CommandDictionary, which is
 * serializable and the command invoker, which contains references to the object
 * and methods and is not serializable.
 *
 * @author tonyj
 */
public class CommandSetBuilder {
    private DictionaryBuilder dictBuilder = new DictionaryBuilder();
    private InputConversionEngine engine = new InputConversionEngine();

    /**
     * Build a command set from an objects annotations. 
     * @param object The object from which annotations will be extracted, and to
     * which invocation requests will be forwarded.
     * @return A CommandSet representing the commands found in the object.
     */
    public CommandSet buildCommandSet(Object object) {
        Class targetClass = object.getClass();
        Dictionary dict = dictBuilder.build(targetClass);
        CommandSetImplementation result = new CommandSetImplementation(dict, object);
        for (Method method : targetClass.getMethods()) {
            Command annotation = method.getAnnotation(Command.class);
            if (annotation != null) {
                result.add(method);
            }
        }
        return result;
    }

    private class CommandSetImplementation implements CommandSet {

        private final Dictionary dict;
        private final Object target;
        private final List<Method> methods = new ArrayList<>();

        private CommandSetImplementation(Dictionary dict, Object target) {
            this.dict = dict;
            this.target = target;
        }

        @Override
        public Dictionary getCommandDictionary() {
            return dict;
        }

        @Override
        public Object invoke(TokenizedCommand tc) throws CommandInvocationException {
            int index = dict.findCommand(tc);
            if (index >= 0) {
                return invoke(target, methods.get(index), tc);
            }
            throw new CommandInvocationException("No handler found for command %s with %d arguments", tc.getCommand(), tc.getArgumentCount());
        }

        private void add(Method method) {
            methods.add(method);
        }

        private Object invoke(Object target, Method method, TokenizedCommand tc) throws CommandInvocationException {
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
                            Array.set(theArray, j, engine.convertArgToType(tc.getArgument(i+j), elemClass));
                        }
                        args[i] = theArray;
                    } else {
                        args[i] = engine.convertArgToType(tc.getArgument(i), parameterTypes[i]);
                    }
                }
                return method.invoke(target, args);
            } catch (IllegalAccessException | IllegalArgumentException ex) {
                throw new CommandInvocationException("Error invoking command", ex);
            } catch (InvocationTargetException ex) {
                throw new CommandInvocationException(ex);
            }
        }
    }
}
