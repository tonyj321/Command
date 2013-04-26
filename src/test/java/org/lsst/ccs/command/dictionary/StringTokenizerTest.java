package org.lsst.ccs.command.dictionary;

import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

/**
 *
 * @author tonyj
 */
public class StringTokenizerTest extends TestCase {

    public StringTokenizerTest(String testName) {
        super(testName);
    }

    public void testTokenize1() {
        List<StringTokenizer.Token> tokenize = StringTokenizer.tokenize(null);
        assertTrue(tokenize.isEmpty());
    }

    public void testTokenize2() {
        List<StringTokenizer.Token> tokenize = StringTokenizer.tokenize("abc");
        assertEquals(1, tokenize.size());
        assertEquals("abc", tokenize.get(0).getString());
    }

    public void testTokenize3() {
        List<StringTokenizer.Token> tokenize = StringTokenizer.tokenize(" \t  abc \t  ");
        assertEquals(1, tokenize.size());
        assertEquals("abc", tokenize.get(0).getString());
    }

    public void testTokenize4() {
        List<StringTokenizer.Token> tokenize = StringTokenizer.tokenize("");
        assertTrue(tokenize.isEmpty());
    }

    public void testTokenize5() {
        List<StringTokenizer.Token> tokenize = StringTokenizer.tokenize("\t");
        assertTrue(tokenize.isEmpty());
    }

    public void testTokenize6() {
        List<StringTokenizer.Token> tokenize = StringTokenizer.tokenize("\tHello\tWorld");
        assertEquals(2, tokenize.size());
        assertEquals("Hello", tokenize.get(0).getString());
        assertEquals("World", tokenize.get(1).getString());
    }

    public void testTokenize7() {
        List<StringTokenizer.Token> tokenize = StringTokenizer.tokenize("\tdon't\t\"do that\"");
        assertEquals(2, tokenize.size());
        assertEquals("don't", tokenize.get(0).getString());
        assertEquals("do that", tokenize.get(1).getString());
    }
    public void testTokenize8() {
        List<StringTokenizer.Token> tokenize = StringTokenizer.tokenize("'Unterminated");
        // Acceptable? or should this throw an exception or wait for more input?
        assertEquals(1, tokenize.size());
        assertEquals("Unterminated", tokenize.get(0).getString());
    }
}
