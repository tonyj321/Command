package org.lsst.ccs.command.demo.remote.jgroups;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.Dictionary;
import org.lsst.ccs.command.CommandInvocationException;
import org.lsst.ccs.command.CommandSet;
import org.lsst.ccs.command.CommandSetBuilder;
import org.lsst.ccs.command.CompositeCommandSet;
import org.lsst.ccs.command.TokenizedCommand;
import org.lsst.ccs.command.remote.CommandClient;
import org.lsst.ccs.command.remote.CommandResponse;

/**
 * A simple CommandClient using JGroups as the communication mechanism.  
 * @author tonyj
 */
public class JGroupsCommandClient extends CompositeCommandSet implements CommandClient, Closeable {
    //FIXME: We should worry about thread safety of command set

    private static final Logger logger = Logger.getLogger(JGroupsCommandClient.class.getName());
    private final JChannel channel;
    private final Exchanger<CommandResponse> exchanger = new Exchanger<>();
    private View view;

    public JGroupsCommandClient() throws Exception {
        channel = new JChannel();
        channel.setReceiver(new ClientReceiverAdapter());
        channel.connect(JGroupsCommandServer.CCS_REMOTE_COMMAND_DEMO);
        CommandSetBuilder builder = new CommandSetBuilder();
        add(builder.buildCommandSet(new BuiltIns()));
        channel.send(new Message(null, JGroupsCommandServer.HELLO));
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    private class ClientReceiverAdapter extends ReceiverAdapter {

        @Override
        public void viewAccepted(View view) {
            JGroupsCommandClient.this.view = view;
            List<CommandSetImplementation> maybeDelete = new ArrayList<>();
            for (CommandSet set : getCommandSets()) {
                if (set instanceof CommandSetImplementation) {
                    maybeDelete.add((CommandSetImplementation) set);
                }
            }
            for (Address address : view) {
                for (Iterator<CommandSetImplementation> it = maybeDelete.iterator(); it.hasNext();) {
                    CommandSetImplementation set = it.next();
                    if (set.getAddress().equals(address)) {
                        it.remove();
                    }
                }
            }

            for (CommandSet set : maybeDelete) {
                remove(set);
            }
        }

        @Override
        public void receive(Message msg) {
            Object obj = msg.getObject();
            if (obj instanceof Dictionary) {
                Address src = msg.getSrc();
                add(new CommandSetImplementation(src, (Dictionary) obj));
            } else if (obj instanceof CommandResponse) {
                try {
                    exchanger.exchange((CommandResponse) obj);
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, "Interrupt while exchanging response", ex);
                }
            }
        }
    }

    private class CommandSetImplementation implements CommandSet {

        private final Address src;
        private final Dictionary dict;

        private CommandSetImplementation(Address src, Dictionary dict) {
            this.src = src;
            this.dict = dict;
        }

        @Override
        public Dictionary getCommandDictionary() {
            return dict;
        }

        @Override
        public Object invoke(TokenizedCommand tc) throws CommandInvocationException {
            try {
                Message msg = new Message(src, tc);
                channel.send(msg);
                return exchanger.exchange(null).getResult();
            } catch (CommandInvocationException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new CommandInvocationException("Unable to send command", ex);
            }
        }

        private Object getAddress() {
            return src;
        }
    }

    public class BuiltIns {

        @Command(description = "Show status of cluster")
        public void status() {
            for (Address address : view) {
                System.out.println(address);
            }
        }
    }
}
