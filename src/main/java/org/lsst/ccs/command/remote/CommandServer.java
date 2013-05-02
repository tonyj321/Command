package org.lsst.ccs.command.remote;

import org.lsst.ccs.command.CommandSet;

/**
 * A command server which waits to receive commands from a remote client
 * @author tonyj
 */
public class CommandServer {
    private final CommandSet commands;
    
    public CommandServer(CommandSet commands) {
        this.commands = commands;
    }
    public CommandSet getCommandSet() {
        return commands;
    }

}
