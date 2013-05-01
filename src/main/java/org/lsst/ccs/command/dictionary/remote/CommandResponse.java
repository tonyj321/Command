package org.lsst.ccs.command.dictionary.remote;

import java.io.Serializable;
import org.lsst.ccs.command.dictionary.CommandSet;

/**
 * The result of invoking a command on a remote server. May consist of <b>either</b>
 * a result (which must be Serializable) or an exception (which by definition must 
 * be serializable).
 * @author tonyj
 */
public class CommandResponse implements Serializable {
    private Serializable result;
    private CommandSet.CommandInvocationException exception;

    public CommandResponse(Serializable result) {
        this.result = result;
    }

    public CommandResponse(CommandSet.CommandInvocationException exception) {
        this.exception = exception;
    }
    
    public Object getResult() throws CommandSet.CommandInvocationException {
        if (exception != null) throw exception;
        return result;
    }
}
