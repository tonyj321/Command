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
        assertEquals(3,dict.size());
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
}
