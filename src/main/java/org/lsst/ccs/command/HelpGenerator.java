package org.lsst.ccs.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.annotations.Argument;

/**
 * Provides help based on information from a command dictionary.
 *
 * @author tonyj
 */
public class HelpGenerator {

    private final Dictionary dict;
    private final PrintWriter out;

    /**
     * Create a HelpGenerator
     *
     * @param out Where the output of the help commands should be sent
     * @param dict The dictionary used to provide help
     */
    public HelpGenerator(PrintWriter out, Dictionary dict) {
        this.out = out;
        this.dict = dict;
    }

    @Command(description = "List available commands")
    public void help() {
        List<DictionaryCommand> sorted = new ArrayList<>();
        for (DictionaryCommand def : dict) {
            sorted.add(def);
        }
        Collections.sort(sorted, new CommandDefinitionComparator());
        for (DictionaryCommand def : sorted) {
            helpForCommand(def);
        }
    }

    @Command(description = "Show help for a single command")
    public void help(@Argument(name = "command") String command) {
        boolean foundCommand = false;
        for (DictionaryCommand def : dict) {
            if (def.getCommandName().equals(command)) {
                foundCommand = true;
                helpForCommand(def);
                for (DictionaryParameter param : def.getParams()) {
                    out.printf("\t%s %s %s\n", param.getName(), param.getType(), param.getDescription());
                }
            }
        }
        if (!foundCommand) throw new IllegalArgumentException("No help found for "+command);
    }

    private void helpForCommand(DictionaryCommand def) {
        StringBuilder builder = new StringBuilder();
        builder.append(def.getCommandName());
        for (DictionaryParameter param : def.getParams()) {
            builder.append(' ').append(param.getName());
        }
        if (def.isVarArgs()) {
            builder.append("...");
        }
        // FIXME: What if builder is > 30 characters, would be nice to 
        // include a new line automatically
        out.printf("%-30s %s\n", builder, def.getDescription());
    }

    /**
     * A comparator used for putting commands into alphabetical order
     */
    private static class CommandDefinitionComparator implements Comparator<DictionaryCommand> {

        @Override
        public int compare(DictionaryCommand o1, DictionaryCommand o2) {
            return o1.getCommandName().compareTo(o2.getCommandName());
        }
    }
}
