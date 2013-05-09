package org.lsst.ccs.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.lsst.ccs.command.demo.RCMReg;

/**
 *
 * @author tonyj
 */
public class HelpGeneratorTest extends TestCase {

    private HelpGenerator help;
    private CRC32 cksum;

    @Override
    protected void setUp() throws Exception {
        DictionaryBuilder builder = new DictionaryBuilder();
        Dictionary dict = builder.build(RCMReg.class);
        cksum = new CRC32();
        PrintWriter printWriter = new PrintWriter(new CheckedOutputStream(new NullOutputStream(), cksum), true);
        help = new HelpGenerator(printWriter, dict);
    }

    /**
     * Test of help method, of class HelpGenerator.
     */
    public void testHelp() {
        help.help();
        assertEquals(3745006164l, cksum.getValue());
    }

    public void testHelp2() {

        help.help("connect");
        assertEquals(4056089828l, cksum.getValue());
    }

    public void testHelp3() {

        try {
            help.help("missing");
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException x) {
            assertEquals(0, cksum.getValue());
        }
    }

    private static class NullOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
        }
    }
}
