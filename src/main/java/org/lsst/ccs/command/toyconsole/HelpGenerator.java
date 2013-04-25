package org.lsst.ccs.command.toyconsole;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.annotations.Parameter;
import org.lsst.ccs.command.dictionary.CommandDefinition;
import org.lsst.ccs.command.dictionary.CommandDictionary;
import org.lsst.ccs.command.dictionary.ParameterDefinition;

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

    @Command(description = "List available commands")
    public void help() {
        List<CommandDefinition> sorted = new ArrayList<>();
        for (CommandDefinition def : dict) {
            sorted.add(def);
        }
        Collections.sort(sorted, new CommandDefinitionComparator());
        for (CommandDefinition def : sorted) {
            StringBuilder builder = new StringBuilder();
            builder.append(def.getCommandName());
            for (ParameterDefinition param : def.getParams()) {
                builder.append(' ').append(param.getName());
            }
            out.printf("%-30s %s\n", builder, def.getDescription());
        }
    }

    @Command(description = "Show help for a single command")
    public void help(@Parameter(name = "command") String command) {
        for (CommandDefinition def : dict) {
            if (def.getCommandName().equals(command)) {
                StringBuilder builder = new StringBuilder();
                builder.append(def.getCommandName());
                for (ParameterDefinition param : def.getParams()) {
                    builder.append(' ').append(param.getName());
                }
                out.printf("%-30s %s\n", builder, def.getDescription());
                for (ParameterDefinition param : def.getParams()) {
                    out.printf("\t%s %s %s\n",param.getName(),param.getType(),param.getDescription());
                }
            }
        }
    }

    private static class CommandDefinitionComparator implements Comparator<CommandDefinition> {

        @Override
        public int compare(CommandDefinition o1, CommandDefinition o2) {
            return o1.getCommandName().compareTo(o2.getCommandName());
        }
    }
}
