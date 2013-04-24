package org.lsst.ccs.command.dictionary;

import java.io.Serializable;

/**
 * A command line that has been split into tokens. This could in future be expanded
 * to also support command options (beginning with - or --)
 * @author tonyj
 */
public class TokenizedCommand implements Serializable {

    private final String[] tokens;

    public TokenizedCommand(String command) {
        // FIXME: Need much better here -- perhaps use cliche tokenizer
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

    public boolean isEmpty() {
        return tokens.length==1 && tokens[0].isEmpty();
    }
}
