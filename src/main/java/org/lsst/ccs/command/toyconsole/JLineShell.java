package org.lsst.ccs.command.toyconsole;

import java.io.IOException;
import java.io.PrintWriter;
import jline.console.ConsoleReader;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.dictionary.CommandSet;
import org.lsst.ccs.command.dictionary.CommandSetBuilder;
import org.lsst.ccs.command.dictionary.CompositeCommandSet;
import org.lsst.ccs.command.dictionary.TokenizedCommand;

/**
 *
 * @author tonyj
 */
public class JLineShell {

    private final CommandSet commands;
    private boolean exitRequested;

    JLineShell(CommandSet userCommands) {
        CompositeCommandSet allCommands = new CompositeCommandSet();
        CommandSetBuilder builder = new CommandSetBuilder();
        allCommands.add(builder.buildCommandSet(new BuiltIns()));
        allCommands.add(userCommands);
        allCommands.add(builder.buildCommandSet(new HelpGenerator(new PrintWriter(System.out, true), allCommands.getCommandDictionary())));
        commands = allCommands;
    }

    public void run() throws IOException {
        ConsoleReader reader = new ConsoleReader();
        while (!exitRequested) {
            String command = reader.readLine(">>>");
            try {
                TokenizedCommand tc = new TokenizedCommand(command);
                Object result = commands.invoke(tc);
                if (result != null) {
                    reader.println("result=" + result);
                }
            } catch (CommandSet.CommandInvocationException ex) {
                reader.println("Error: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        CompositeCommandSet commands = new CompositeCommandSet();
        JLineShell shell = new JLineShell(commands);
        shell.run();
    }

    public class BuiltIns {

        @Command(description = "Exit from the shell")
        public void exit() {
            exitRequested = true;
        }
    }
}
