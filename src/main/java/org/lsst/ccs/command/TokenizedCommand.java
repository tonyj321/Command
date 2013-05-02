package org.lsst.ccs.command;

import java.io.Serializable;
import java.util.List;
import org.lsst.ccs.command.StringTokenizer.Token;

/**
 * A command line that has been split into tokens. This could in future be expanded
 * to also support command options (beginning with - or --)
 * @author tonyj
 */
public class TokenizedCommand implements Serializable {

    private final List<Token> tokens;

    public TokenizedCommand(String command) {
        tokens = StringTokenizer.tokenize(command);
    }

    String getCommand() {
        return tokens.get(0).getString();
    }

    String getArgument(int index) {
        return tokens.get(index + 1).getString();
    }

    int getArgumentCount() {
        return Math.max(tokens.size() - 1, 0);
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }
}
