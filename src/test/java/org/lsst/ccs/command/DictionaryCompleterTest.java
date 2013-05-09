package org.lsst.ccs.command;

import java.util.ArrayList;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.lsst.ccs.command.demo.DemoCommands;

/**
 *
 * @author tonyj
 */
public class DictionaryCompleterTest extends TestCase {

    private Dictionary dict;

    public DictionaryCompleterTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        DictionaryBuilder builder = new DictionaryBuilder();
        dict = builder.build(DemoCommands.class);
    }

    public void testComplete() {
        DictionaryCompleter completer = new DictionaryCompleter(dict);
        ArrayList<CharSequence> list = new ArrayList<>();
        int pos = completer.complete("", 0, list);
        assertEquals(0, pos);
        assertEquals(5, list.size());
        assertEquals("add ", list.get(0));
        list.clear();
        pos = completer.complete(" ", 1, list);
        assertEquals(1, pos);
        assertEquals(5, list.size());
        assertEquals("add ", list.get(0));
        assertEquals("error", list.get(3));
        list.clear();
        pos = completer.complete("\ta", 2, list);
        assertEquals(1, pos);
        assertEquals(2, list.size());
        assertEquals("add ", list.get(0));
        assertEquals("addAll ", list.get(1));
        list.clear();
        pos = completer.complete("\tdayOfWeek ", 11, list);
        assertEquals(11, pos);
        assertEquals(7, list.size());
        assertEquals("SUNDAY", list.get(0));
        list.clear();
        pos = completer.complete("\tdayOfWeek T", 12, list);
        assertEquals(11, pos);
        assertEquals(2, list.size());
        assertEquals("TUESDAY", list.get(0));
        list.clear();
        pos = completer.complete("\tdayOfWeek TU", 13, list);
        assertEquals(11, pos);
        assertEquals(1, list.size());
        assertEquals("TUESDAY", list.get(0));
        list.clear();
        pos = completer.complete("\tdayOfWeek TUW", 14, list);
        assertEquals(11, pos);
        assertTrue(list.isEmpty());
    }
}
