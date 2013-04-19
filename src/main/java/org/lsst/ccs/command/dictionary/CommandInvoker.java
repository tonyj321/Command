package org.lsst.ccs.command.dictionary;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.lsst.ccs.command.cliche.InputConversionEngine;
import org.lsst.ccs.command.cliche.Token;
import org.lsst.ccs.command.cliche.TokenException;

/**
 * A toy invoker. Does not implement all features needed for use in CCS
 * framework (for example command responses, checking levels, etc). Should
 * probably use some of the existing utilities in CCS utilities for real
 * implementation.
 *
 * @author tonyj
 */
public class CommandInvoker {

    private final InputConversionEngine ice;

    public CommandInvoker(InputConversionEngine ice) {
        this.ice = ice;
    }

    public Object invoke(CommandDictionary dict, Object target, String command) throws CommandInvocationException, TokenException {
        List<Token> tokens = Token.tokenize(command);
        Token token0 = tokens.get(0);
        for (CommandDefinition def : dict) {
            if (token0.getString().equals(def.getCommandName())) {
                return invoke(def,target,tokens);
            }
        }
        throw new CommandInvocationException("No matching command found: "+token0);
    }
    private Object invoke(CommandDefinition def, Object target, List<Token> tokens) throws CommandInvocationException, TokenException {
        if (!def.getTargetName().equals(target.getClass().getName())) {
            throw new CommandInvocationException("Command and target do not match");
        }
        // Reify the method, maybe we should use some of Bernard utilities here, this
        // is certainly an ugly way to do it, and unnecessary if we do not want to support
        // method overloading.
        for (Method method : target.getClass().getMethods()) {
            if (method.toGenericString().equals(def.getMethodName())) {
                return invokeMethod(def, target, method, tokens);
            }
        }
        throw new CommandInvocationException("No matching method found");
    }

    private Object invokeMethod(CommandDefinition def, Object target, Method method, List<Token> tokens) throws CommandInvocationException, TokenException {
        Object[] args = ice.convertToParameters(tokens, method.getParameterTypes(), def.isVarArgs());
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new CommandInvocationException(ex);
        }
    }

    public static class CommandInvocationException extends Exception {

        public CommandInvocationException(String message) {
            super(message);
        }

        private CommandInvocationException(java.lang.Exception ex) {
            super(ex);
        }
    }
}
