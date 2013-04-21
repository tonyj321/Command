package org.lsst.ccs.command.dictionary;

/**
 *
 * @author tonyj
 */
public class TokenizedCommand {

    private final String[] tokens;

    public TokenizedCommand(String command) {
        tokens = command.split("\\s+");
    }

    String getCommand() {
        return tokens[0];
    }

    String getArgument(int index) {
        return tokens[index + 1];
    }

    int getArgumentCount() {
        return tokens.length - 1;
    }
}
