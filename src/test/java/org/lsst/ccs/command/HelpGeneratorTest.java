package org.lsst.ccs.command;

import java.io.PrintWriter;
import junit.framework.TestCase;
import org.lsst.ccs.command.demo.RCMReg;

/**
 *
 * @author tonyj
 */
public class HelpGeneratorTest extends TestCase {

    /**
     * Test of help method, of class HelpGenerator.
     */
    public void testHelp() throws CommandInvocationException {
        CommandSetBuilder builder = new CommandSetBuilder();
        Dictionary dict = builder.buildCommandSet(new RCMReg()).getCommandDictionary();        
        HelpGenerator help = new HelpGenerator(new PrintWriter(System.out,true),dict);
        help.help();
        CommandSet commands = builder.buildCommandSet(help);
        assertEquals(2,commands.getCommandDictionary().size());
        commands.invoke(new TokenizedCommand("help"));
    }
}
