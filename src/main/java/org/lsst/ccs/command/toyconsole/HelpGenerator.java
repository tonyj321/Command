package org.lsst.ccs.command.toyconsole;

import java.io.PrintWriter;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.dictionary.CommandDefinition;
import org.lsst.ccs.command.dictionary.SerializableCommandDictionary;

/**
 *
 * @author tonyj
 */
public class HelpGenerator {
    private final SerializableCommandDictionary dict;
    private final PrintWriter out;
    
    HelpGenerator(PrintWriter out, SerializableCommandDictionary dict) {
        this.out = out;
        this.dict = dict;
    }
    
    @Command(description="List available commands")
    void help() {
        for (CommandDefinition def : dict) {
            out.println(def.getCommandName());
        }
    }
}
