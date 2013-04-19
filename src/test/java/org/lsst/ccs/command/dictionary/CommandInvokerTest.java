package org.lsst.ccs.command.dictionary;

import junit.framework.TestCase;
import org.lsst.ccs.command.cliche.InputConversionEngine;
import org.lsst.ccs.command.cliche.TokenException;
import org.lsst.ccs.command.demo.DemoCommands;

/**
 *
 * @author tonyj
 */
public class CommandInvokerTest extends TestCase {
    private SerializableCommandDictionary dict;

    @Override
    protected void setUp() throws Exception {
        DictionaryBuilder builder = new DictionaryBuilder();
        dict = builder.createCommandDictionary(DemoCommands.class);
    }
    
    /**
     * Test of createCommandDictionary method, of class DictionaryBuilder.
     */
    public void testInvokeCommand() throws CommandInvoker.CommandInvocationException, TokenException {
        DemoCommands target = new DemoCommands();
        CommandInvoker invoker = new CommandInvoker(new InputConversionEngine());
        Object result = invoker.invoke(dict, target, "getTemperature 5");
        assertTrue(result instanceof Double);
    }

}
