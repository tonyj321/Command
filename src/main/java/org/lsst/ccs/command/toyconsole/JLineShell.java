package org.lsst.ccs.command.toyconsole;

import org.lsst.ccs.command.dictionary.HelpGenerator;
import org.lsst.ccs.command.dictionary.DictionaryCompleter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import jline.console.history.History;
import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.annotations.Parameter;
import org.lsst.ccs.command.dictionary.CommandDictionary;
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
    private final ConsoleReader reader;
    private final PrintWriter printWriter;
    private CommandSet.CommandInvocationException lastException;

    public JLineShell(CommandSet userCommands) throws IOException {
        reader = new ConsoleReader();
        reader.setPrompt(">>>");
        printWriter = new PrintWriter(reader.getOutput(), true);
        CompositeCommandSet allCommands = new CompositeCommandSet();
        CommandSetBuilder builder = new CommandSetBuilder();
        allCommands.add(builder.buildCommandSet(new BuiltIns()));
        allCommands.add(userCommands);
        CommandDictionary commandDictionary = allCommands.getCommandDictionary();
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
            } catch (CommandSet.CommandInvocationException ex) {
                reader.println("Error: " + ex.getMessage());
                lastException = ex;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        CompositeCommandSet noCommands = new CompositeCommandSet();
        JLineShell shell = new JLineShell(noCommands);
        shell.run();
    }

    public enum SetCommands {

        prompt
    };

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
