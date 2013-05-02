package org.lsst.ccs.command.demo.shell;

import org.lsst.ccs.command.HelpGenerator;
import org.lsst.ccs.command.DictionaryCompleter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import jline.console.history.History;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.annotations.Parameter;
import org.lsst.ccs.command.Dictionary;
import org.lsst.ccs.command.CommandInvocationException;
import org.lsst.ccs.command.CommandSet;
import org.lsst.ccs.command.CommandSetBuilder;
import org.lsst.ccs.command.CompositeCommandSet;
import org.lsst.ccs.command.TokenizedCommand;

/**
 * A simple shell for playing with the command parsing classes.
 * This class is designed to be run from a terminal and uses JLine to 
 * interact with the user. The command shell has some built-in functionality,
 * including the ability to provide help, and the ability to do tab completion.
 * @author tonyj
 */
public class JLineShell {

    private final CommandSet commands;
    private boolean exitRequested;
    private final ConsoleReader reader;
    private final PrintWriter printWriter;
    private CommandInvocationException lastException;

    /** Creates a JLineShell with the given set of user commands.
     * @param userCommands The user defined commands which will be merged with 
     * the built-in commands provided by the shell itself. The CommandSet passed
     * in can change dynamically (for example if it is in fact a CompositeCommandSet
     * commands can be added and removed dynamically).
     * @throws IOException If something goes horribly wrong.
     */
    public JLineShell(CommandSet userCommands) throws IOException {
        reader = new ConsoleReader();
        reader.setPrompt(">>>");
        printWriter = new PrintWriter(reader.getOutput(), true);
        printWriter.println("Type help for list of available commands");
        CompositeCommandSet allCommands = new CompositeCommandSet();
        CommandSetBuilder builder = new CommandSetBuilder();
        allCommands.add(builder.buildCommandSet(new BuiltIns()));
        allCommands.add(userCommands);
        Dictionary commandDictionary = allCommands.getCommandDictionary();
        final DictionaryCompleter dictionaryCompleter = new DictionaryCompleter(commandDictionary);
        allCommands.add(builder.buildCommandSet(new HelpGenerator(printWriter, commandDictionary)));
        commands = allCommands;
        Completer completer = new Completer() {
            @Override
            public int complete(String string, int i, List<CharSequence> list) {
                return dictionaryCompleter.complete(string, i, list);
            }
        };
        reader.addCompleter(completer);
    }
    /**
     * Run the command shell. This method does not return until the user exits
     * from the shell.
     * @throws IOException If something goes horribly wrong. 
     */
    public void run() throws IOException {
        while (!exitRequested) {
            String command = reader.readLine();
            if (command == null) {
                break;
            }
            try {
                TokenizedCommand tc = new TokenizedCommand(command);
                if (!tc.isEmpty()) {
                    Object result = commands.invoke(tc);
                    if (result != null) {
                        reader.println("result=" + result);
                    }
                }
            } catch (    CommandInvocationException ex) {
                reader.println("Error: " + ex.getMessage());
                lastException = ex;
            }
        }
    }

    /** An enumeration of the arguments to the set command. Note that the 
     * built in tab completion understands enumerations.
     */
    public enum SetCommands {

        prompt
    };

    /** The set of built in commands.
     */
    public class BuiltIns {

        @Command(description = "Exit from the shell")
        public void exit() {
            exitRequested = true;
        }

        @Command(description = "Show command history")
        public void history() {
            History history = reader.getHistory();
            for (int i = 0; i < history.size(); i++) {
                printWriter.printf("%3d: %s\n", i, history.get(i));
            }
        }

        @Command(description = "Show the full stacktrace of the most recent error", abbrev = "st")
        public void stacktrace() {
            if (lastException != null) {
                lastException.printStackTrace(printWriter);
            }
        }

        @Command(description = "Modify various settings")
        public void set(@Parameter(name = "item") SetCommands what, @Parameter(name = "value") String value) {
            switch (what) {
                case prompt:
                    reader.setPrompt(value);
            }
        }
    }
}
