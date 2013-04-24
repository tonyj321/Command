package org.lsst.ccs.command.dictionary.remote.jgroups;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.lsst.ccs.command.demo.DemoCommands;
import org.lsst.ccs.command.dictionary.CommandSet;
import org.lsst.ccs.command.dictionary.CommandSetBuilder;
import org.lsst.ccs.command.dictionary.TokenizedCommand;
import org.lsst.ccs.command.dictionary.remote.CommandResponse;
import org.lsst.ccs.command.dictionary.remote.CommandServer;

/**
 * A server to make a CommandSet available remotely using JGroups.
 * @author tonyj
 */
public class JGroupsCommandServer extends CommandServer implements Closeable {

    private static final Logger logger = Logger.getLogger(JGroupsCommandServer.class.getName());
    static final String CCS_REMOTE_COMMAND_DEMO = "CCSRemoteCommandDemo";
    static final String HELLO = "Hello";
    private final JChannel channel;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    JGroupsCommandServer(CommandSet set) throws Exception {
        super(set);
        channel = new JChannel();
        channel.setReceiver(new ReceiverAdapterImpl());
        channel.connect(CCS_REMOTE_COMMAND_DEMO);
        Message msg = new Message(null, set.getCommandDictionary());
        channel.send(msg);
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();
        try {
            executor.awaitTermination(0, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.log(Level.INFO, "Interrupted while closing server", ex);
        }
        channel.close();
    }

    private class ReceiverAdapterImpl extends ReceiverAdapter {

        @Override
        public void receive(Message msg) {
            System.out.println("msg=" + msg);
            final Object obj = msg.getObject();
            if (HELLO.equals(obj)) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message(null, getCommandSet().getCommandDictionary());
                        send(msg);
                    }
                };
                executor.submit(runnable);
            } else if (obj instanceof TokenizedCommand) {
                final Address src = msg.getSrc();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object result = getCommandSet().invoke((TokenizedCommand) obj);
                            CommandResponse response = new CommandResponse((Serializable) result);
                            send(new Message(src, response));
                        } catch (CommandSet.CommandInvocationException ex) {
                            send(new Message(src, new CommandResponse(ex)));
                        }
                    }
                };
                executor.submit(runnable);
            }
        }
    }

    private void send(Message msg) {
        try {
            channel.send(msg);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error while sending message", ex);
        }
    }

    public static void main(String[] args) throws Exception {
        CommandSetBuilder builder = new CommandSetBuilder();
        JGroupsCommandServer server = new JGroupsCommandServer(builder.buildCommandSet(new DemoCommands()));
    }
}
