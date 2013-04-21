package org.lsst.ccs.command.toyconsole;

import java.io.PrintWriter;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.dictionary.CommandDefinition;
import org.lsst.ccs.command.dictionary.CommandDictionary;

/**
 *
 * @author tonyj
 */
public class HelpGenerator {
    private final CommandDictionary dict;
    private final PrintWriter out;
    
    HelpGenerator(PrintWriter out, CommandDictionary dict) {
        this.out = out;
        this.dict = dict;
    }
    
    @Command(description="List available commands")
    public void help() {
        for (CommandDefinition def : dict) {
            out.println(def.getCommandName());
        }
    }
}
