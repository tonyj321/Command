package org.lsst.ccs.command.remote;

import org.lsst.ccs.command.CommandSet;

/**
 * A CommandSet which acts as a proxy for a remote CommandServer. 
 * The contents of the command set may change as new servers register 
 * or unregister.
 * @author tonyj
 */
public interface CommandClient extends CommandSet {
    
}
