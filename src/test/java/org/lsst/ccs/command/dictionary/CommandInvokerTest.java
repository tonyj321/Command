package org.lsst.ccs.command.dictionary;

import junit.framework.TestCase;
import org.lsst.ccs.command.demo.DemoCommands;

/**
 *
 * @author tonyj
 */
public class CommandInvokerTest extends TestCase {
    private CommandSet dict;

    @Override
    protected void setUp() throws Exception {
        CommandSetBuilder builder = new CommandSetBuilder();
        dict = builder.buildCommandSet(new DemoCommands());
    }
    
    /**
     * Test of createCommandDictionary method, of class DictionaryBuilder.
     */
    public void testInvokeCommand() throws CommandSet.CommandInvocationException {
        TokenizedCommand tc = new TokenizedCommand("getTemperature 5");
        Object invoke = dict.invoke(tc);
    }

}
