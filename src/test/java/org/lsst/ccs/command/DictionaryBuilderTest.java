package org.lsst.ccs.command;

import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.lsst.ccs.command.demo.DemoCommands;
import org.lsst.ccs.command.demo.RCMReg;

/**
 *
 * @author tonyj
 */
public class DictionaryBuilderTest extends TestCase {


    public void testCreateCommandDictionary() {
        DictionaryBuilder builder = new DictionaryBuilder();
        Dictionary dict = builder.build(DemoCommands.class);
        assertEquals(5,dict.size());
        DictionaryCommand cd = dict.get(0);
        assertEquals(2,cd.getParams().length);
        assertEquals("add",cd.getCommandName());
        assertFalse(cd.isVarArgs());
    }
    public void testCreateCommandDictionary2() {
        DictionaryBuilder builder = new DictionaryBuilder();
        Dictionary dict = builder.build(RCMReg.class);
        assertEquals(3,dict.size());
    }
    
    public void testCommandDictionaryEnum() {
        DictionaryBuilder builder = new DictionaryBuilder();
        Dictionary dict = builder.build(DemoCommands.class);
        int index = dict.findCommand(new TokenizedCommand("dayOfWeek 123"));
        DictionaryCommand dc = dict.get(index);
        assertEquals("dayOfWeek",dc.getCommandName());
        assertEquals(1,dc.getParams().length);
        assertEquals("Day",dc.getParams()[0].getType());
        assertEquals(7,dc.getParams()[0].getValues().size());
    }
}
