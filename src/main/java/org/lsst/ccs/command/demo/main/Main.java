package org.lsst.ccs.command.demo.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.lsst.ccs.command.demo.DemoCommands;
import org.lsst.ccs.command.CommandSetBuilder;
import org.lsst.ccs.command.demo.remote.jgroups.JGroupsCommandClient;
import org.lsst.ccs.command.demo.remote.jgroups.JGroupsCommandServer;
import org.lsst.ccs.command.demo.shell.JLineShell;

/**
 * This main class acts as a steering class for the demos.
 *
 * @author tonyj
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        if (args.length > 0) {
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
            GUIPanel gui = new GUIPanel();
            JFrame frame = new JFrame("CCS Command Demo");
            frame.setContentPane(gui);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
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

    private static class GUIPanel extends JPanel implements ActionListener {

        public GUIPanel() {
            addButton(new JButton("Open Local Shell"), "shell");
            addButton(new JButton("Open Distributed Client Shell"), "command");
            addButton(new JButton("Open Distributed Command Shell"), "server");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "shell":
                    runShell();
                    break;
                case "client":
                    runClient();
                    break;
                case "server":
                    runServer();
                    break;
            }           
        }
        
        private void runShell() {
            
        }
        
        private void runClient() {
            
        }
        
        private void runServer() {
            
        }

        private void addButton(JButton jButton, String command) {
            add(jButton);
            jButton.setActionCommand(command);
            jButton.addActionListener(this);
        }
    }
}
