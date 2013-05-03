package org.lsst.ccs.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
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
        DictionaryBuilder builder = new DictionaryBuilder();
        Dictionary dict = builder.build(RCMReg.class);
        CRC32 cksum = new CRC32();
        PrintWriter printWriter = new PrintWriter(new CheckedOutputStream(new NullOutputStream(), cksum));
        HelpGenerator help = new HelpGenerator(printWriter, dict);
        help.help();
        printWriter.flush();
        assertEquals(3745006164l, cksum.getValue());
        help.help("connect");
        printWriter.flush();
        assertEquals(1961388607l, cksum.getValue());
    }

    private static class NullOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
        }
    }
}
