package org.lsst.ccs.command.demo.main;

import java.io.IOException;
import org.lsst.ccs.command.demo.DemoCommands;
import org.lsst.ccs.command.dictionary.CommandSetBuilder;
import org.lsst.ccs.command.demo.remote.jgroups.JGroupsCommandClient;
import org.lsst.ccs.command.demo.remote.jgroups.JGroupsCommandServer;
import org.lsst.ccs.command.demo.shell.JLineShell;

/**
 * This main class acts as a steering class for the demos.
 * @author tonyj
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Main main = new Main();

        if (args.length>0) {
            String command = args[0];
            switch (command) {
                case "shell":
                    main.runShell();
                    break;
                case "client":
                    main.runClient();
                    break;
                case "server":
                    main.runServer();
                    break;
                default: 
                    help();
            }
        } else {
            help();
        }

    }

    private static void help() {
        System.out.println("java -jar <jar-name> <command>\n");
        System.out.println("where command is one of:\n");
        System.out.println("\tshell -- run the interactive shell locally");
        System.out.println("\tserver -- run a command server");
        System.out.println("\tclient -- run a command client server");
    }

    private void runShell() throws IOException {
        CommandSetBuilder builder = new CommandSetBuilder();
        JLineShell shell = new JLineShell(builder.buildCommandSet(new DemoCommands()));
        shell.run();
    }

    private void runClient() throws Exception {
        try (JGroupsCommandClient client = new JGroupsCommandClient()) {
            JLineShell shell = new JLineShell(client);
            shell.run();
        }
    }

    private void runServer() throws Exception {
        CommandSetBuilder builder = new CommandSetBuilder();
        JGroupsCommandServer server = new JGroupsCommandServer(builder.buildCommandSet(new DemoCommands()));
    }
}

