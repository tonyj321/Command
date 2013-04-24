package org.lsst.ccs.command.dictionary.remote;

import org.lsst.ccs.command.dictionary.CommandSet;

/**
 * A CommandSet which acts as a proxy for a remote CommandServer. 
 * The contents of the command set may change as new servers register 
 * or unregister.
 * @author tonyj
 */
public interface CommandClient extends CommandSet {
    
}
