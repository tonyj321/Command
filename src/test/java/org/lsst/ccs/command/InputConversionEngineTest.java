package org.lsst.ccs.command;

import java.util.Random;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;

/**
 *
 * @author tonyj
 */
public class InputConversionEngineTest extends TestCase {

    private InputConversionEngine inputConversionEngine;

    public InputConversionEngineTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        inputConversionEngine = new InputConversionEngine();
    }

    public void testConvertArgToType() throws Exception {
        Object result = inputConversionEngine.convertArgToType("1234", Integer.TYPE);
        assertEquals(1234, result);
    }

    public void testConvertArgToType2() throws Exception {
        Object result = inputConversionEngine.convertArgToType("1234", Integer.class);
        assertEquals(1234, result);
    }

    public void testConvertArgToType3() throws Exception {
        Object result = inputConversionEngine.convertArgToType("1234", Float.TYPE);
        assertEquals(1234.0f, result);
    }

    public void testConvertArgToType4() throws Exception {
        Object result = inputConversionEngine.convertArgToType("1234", Double.TYPE);
        assertEquals(1234.0, result);
    }

    public void testConvertArgToType11() throws Exception {
        Object result = inputConversionEngine.convertArgToType("TrUe", Boolean.TYPE);
        assertEquals(true, result);
    }
    
    public void testConvertArgToType5() throws Exception {
        Object result = inputConversionEngine.convertArgToType("1234", String.class);
        assertEquals("1234", result);
    }

    public void testConvertArgToType6() throws Exception {
        Object result = inputConversionEngine.convertArgToType("1234", RandomObject.class);
        assertEquals("1234", ((RandomObject) result).getArg());
    }

    public void testConvertArgToType7() throws Exception {
        try {
            inputConversionEngine.convertArgToType("1234", Random.class);
            fail("Should have thrown an exception");
        } catch (CommandInvocationException x) {
            assertEquals("Can't convert string 1234 to class java.util.Random", x.getMessage());
        }
    }

    public void testConvertArgToType8() throws Exception {
        try {
            inputConversionEngine.convertArgToType("4567", RandomObject.class);
            fail("Should have thrown an exception");
        } catch (CommandInvocationException x) {
            assertEquals("Error instantiating class org.lsst.ccs.command.InputConversionEngineTest$RandomObject using string 4567", x.getMessage());
        }
    }

    public void testConvertArgToType9() throws Exception {
        try {
            inputConversionEngine.convertArgToType("1234", Boolean.TYPE);
            fail("Should have thrown an exception");
        } catch (CommandInvocationException x) {
            assertEquals("Can't convert 1234 to Boolean", x.getMessage());
        }
    }

    public void testConvertArgToType10() throws Exception {
        try {
            inputConversionEngine.convertArgToType("1234.5", Integer.TYPE);
            fail("Should have thrown an exception");
        } catch (CommandInvocationException x) {
            assertEquals("Can't convert string 1234.5 to class int",x.getMessage());
        }
    }

    private static class RandomObject {

        private final String arg;
        // Note, the constructor must be public for this to work.

        public RandomObject(String arg) {
            if (!"1234".equals(arg)) {
                throw new IllegalArgumentException("Illegal value " + arg);
            }
            this.arg = arg;
        }

        public String getArg() {
            return arg;
        }
    }
}
