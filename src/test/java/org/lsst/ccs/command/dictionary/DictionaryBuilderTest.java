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
        DictionaryBuilder builder = new DictionaryBuilder();
        SerializableCommandDictionary dict = builder.createCommandDictionary(DemoCommands.class);
        assertEquals(1,dict.size());
        CommandDefinition cd = dict.get(0);
        assertEquals(DemoCommands.class.getName(),cd.getTargetName());
        assertEquals(1,cd.getParams().length);
    }
    public void testCreateCommandDictionary2() {
        DictionaryBuilder builder = new DictionaryBuilder();
        SerializableCommandDictionary dict = builder.createCommandDictionary(RCMReg.class);
        assertEquals(3,dict.size());
    }
}
