package org.lsst.ccs.command.demo.shell;

import com.wittams.gritty.Questioner;
import com.wittams.gritty.RequestOrigin;
import com.wittams.gritty.ResizePanelDelegate;
import com.wittams.gritty.Tty;
import com.wittams.gritty.swing.GrittyTerminal;
import com.wittams.gritty.swing.TermPanel;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JFrame;
import jline.Terminal;
import jline.console.ConsoleReader;
import org.lsst.ccs.command.CommandSet;
import org.lsst.ccs.command.CommandSetBuilder;
import org.lsst.ccs.command.demo.DemoCommands;

/**
 * This is an example of embedding a JLine compatible terminal inside a swing
 * component. This could be used to make the command line tools available inside
 * the JAS based console. It uses a terminal emulator called gritty which I found
 * <a href="https://code.google.com/p/gritty/">here</a>. This does not seem very
 * complete or well supported, but perhaps adequate to our needs.
 * 
 * //FIXME: Resizing not really working
 * //FIXME: Whole application quits when window closed
 * @author tonyj
 */
public class SwingShell {

    private GrittyTerminal terminal;
    private final JLineShell shell;
    private final JFrame frame;

    public SwingShell(CommandSet userCommands, String title) throws IOException {
        frame = new JFrame(title);
        terminal = new GrittyTerminal();
        TermPanel termPanel = terminal.getTermPanel();
        frame.setContentPane(terminal);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        termPanel.setVisible(true);
        frame.setVisible(true);
        frame.setResizable(true);

        termPanel.setResizePanelDelegate(new ResizePanelDelegate() {
            @Override
            public void resizedPanel(final Dimension pixelDimension, final RequestOrigin origin) {
                if (origin == RequestOrigin.Remote) {
                    sizeFrameForTerm(frame);
                }
            }
        });
        final TtyImpl tty = new TtyImpl();

        terminal.setTty(tty);
        ConsoleReader reader = new ConsoleReader(tty.getInputStream(), tty.getOutputStream(), new SwingTerminal());
        shell = new JLineShell(userCommands, reader);
    }

    public void run() throws IOException {
        terminal.start();
        try {
            shell.run();
        } finally {
            terminal.stop();
            frame.setVisible(false);
        }
    }

    public static void main(String[] args) throws IOException {
        CommandSetBuilder builder = new CommandSetBuilder();
        SwingShell swingShell = new SwingShell(builder.buildCommandSet(new DemoCommands()),"Swing Shell");
        swingShell.run();
    }

    private void sizeFrameForTerm(final JFrame frame) {
        Dimension d = terminal.getPreferredSize();

        d.width += frame.getWidth() - frame.getContentPane().getWidth();
        d.height += frame.getHeight() - frame.getContentPane().getHeight();
        frame.setSize(d);
    }

    private static class SwingTerminal implements Terminal {

        @Override
        public void init() throws Exception {
        }

        @Override
        public void restore() throws Exception {
        }

        @Override
        public void reset() throws Exception {
        }

        @Override
        public boolean isSupported() {
            return true;
        }

        @Override
        public int getWidth() {
            //FIXME: should be calculated based on size of window
            return 80;
        }

        @Override
        public int getHeight() {
            //FIXME: should be calculated based on size of window
            return 100;
        }

        @Override
        public boolean isAnsiSupported() {
            return false;
        }

        @Override
        public OutputStream wrapOutIfNeeded(OutputStream out) {
            return out;
        }

        @Override
        public InputStream wrapInIfNeeded(InputStream in) throws IOException {
            return in;
        }

        @Override
        public boolean hasWeirdWrap() {
            return false;
        }

        @Override
        public boolean isEchoEnabled() {
            return false;
        }

        @Override
        public void setEchoEnabled(boolean bln) {
        }
    }

    private class TtyImpl implements Tty {

        private final BlockingQueue<byte[]> inputQueue = new LinkedBlockingQueue<>();
        private final BlockingQueue<byte[]> outputQueue = new LinkedBlockingQueue<>();
        private final TtyInputStream inputStream;
        private final TtyOutputStream outputStream;

        public TtyImpl() {
            inputStream = new TtyInputStream();
            outputStream = new TtyOutputStream();
        }

        @Override
        public boolean init(Questioner qstnr) {
            return true;
        }

        @Override
        public void close() {
        }

        @Override
        public void resize(Dimension dmnsn, Dimension dmnsn1) {
        }

        @Override
        public String getName() {
            return "Test";
        }

        @Override
        public int read(byte[] bytes, int i, int i1) throws IOException {
            try {
                byte[] src = outputQueue.take();
                // FIXME: need to handle case where src does not fit in target
                System.arraycopy(src, 0, bytes, i, src.length);
                return src.length;
            } catch (InterruptedException ex) {
                throw new InterruptedIOException();
            }
        }

        @Override
        public void write(byte[] bytes) throws IOException {
            try {
                inputQueue.put(bytes);
            } catch (InterruptedException ex) {
                throw new InterruptedIOException();
            }
        }

        public TtyInputStream getInputStream() {
            return inputStream;
        }

        public TtyOutputStream getOutputStream() {
            return outputStream;
        }

        private class TtyInputStream extends InputStream {

            private byte[] currentBuffer;
            private int pos;

            @Override
            public int read() throws IOException {
                try {
                    while (currentBuffer == null || currentBuffer.length <= pos) {
                        currentBuffer = inputQueue.take();
                        pos = 0;
                    }
                    return currentBuffer[pos++];
                } catch (InterruptedException interruptedException) {
                    throw new InterruptedIOException();
                }
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                try {
                    if (currentBuffer == null || currentBuffer.length <= pos) {
                        currentBuffer = inputQueue.take();
                        pos = 0;
                    }
                    int actual = Math.min(len, currentBuffer.length - pos);
                    System.arraycopy(currentBuffer, pos, b, off, actual);
                    pos += actual;
                    return actual;
                } catch (InterruptedException interruptedException) {
                    throw new InterruptedIOException();
                }
            }
        }

        private class TtyOutputStream extends OutputStream {

            @Override
            public void write(int b) throws IOException {
                try {
                    byte[] buffer = new byte[1];
                    buffer[0] = (byte) b;
                    outputQueue.put(buffer);
                    if (b == 0xa) {
                        byte[] buffer2 = new byte[1];
                        buffer2[0] = 0xd;
                        outputQueue.put(buffer2);
                    }
                } catch (InterruptedException interruptedException) {
                    throw new InterruptedIOException();
                }
            }
        }
    }
}
