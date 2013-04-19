package org.lsst.ccs.command.dictionary;

/**
 * A command dictionary contains all the information needed to provide
 * help and perform tab completion. It does not by itself provide sufficient
 * functionality to invoke a command, for this a combination of a command dictionary
 * and a command invoker are required.
 * @author tonyj
 */
public interface CommandDictionary extends Iterable<CommandDefinition> {
    
}
