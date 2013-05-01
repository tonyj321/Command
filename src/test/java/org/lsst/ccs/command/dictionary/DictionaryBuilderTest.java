package org.lsst.ccs.command.dictionary;

import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.lsst.ccs.command.demo.DemoCommands;
import org.lsst.ccs.command.demo.RCMReg;

/**
 *
 * @author tonyj
 */
public class DictionaryBuilderTest extends TestCase {

    /**
     * Test of createCommandDictionary method, of class DictionaryBuilder.
     */
    public void testCreateCommandDictionary() {
        CommandSetBuilder builder = new CommandSetBuilder();
        CommandDictionary dict = builder.buildCommandSet(new DemoCommands()).getCommandDictionary();
        assertEquals(3,dict.size());
        CommandDefinition cd = dict.get(0);
        assertEquals(2,cd.getParams().length);
    }
    public void testCreateCommandDictionary2() {
        CommandSetBuilder builder = new CommandSetBuilder();
        CommandDictionary dict = builder.buildCommandSet(new RCMReg()).getCommandDictionary();
        assertEquals(3,dict.size());
    }
}
