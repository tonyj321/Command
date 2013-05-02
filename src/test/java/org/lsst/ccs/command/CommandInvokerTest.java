package org.lsst.ccs.command;

import static junit.framework.Assert.assertEquals;
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

    public void testInvokeCommand() throws CommandInvocationException {
        TokenizedCommand tc = new TokenizedCommand("add 5 6");
        Object result = dict.invoke(tc);
        assertEquals(11.0,result);
    }

    public void testInvokeCommand2() throws CommandInvocationException {
        TokenizedCommand tc = new TokenizedCommand("addAll 5 6 7");
        Object result = dict.invoke(tc);
        assertEquals(18.0,result);
    }

    public void testInvokeCommand3() throws CommandInvocationException {
        TokenizedCommand tc = new TokenizedCommand("add 1");
        try {
            dict.invoke(tc);
            fail("Exception should have been thrown");
        } catch (CommandInvocationException x) {
            // OK, this is what was expected
        }
    }
}
