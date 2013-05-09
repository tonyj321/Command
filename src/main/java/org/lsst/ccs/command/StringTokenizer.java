package org.lsst.ccs.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts an input command into a series of tokens.
 * @author tonyj
 */
class StringTokenizer {

    /**
     * Do we really need this, is was used by cliche to by able to provide
     * feedback on where in the command line and illegal token was found, but
     * we do not currently use this functionality.
     */
    static class Token implements Serializable {

        private int index;
        private String string;

        Token(int index, String string) {
            super();
            this.index = index;
            this.string = string;
        }

        int getIndex() {
            return index;
        }

        String getString() {
            return string;
        }
    }

    private enum State {

        WHITESPACE, WORD, STRINGDQ, STRINGSQ, COMMENT
    };

    public static List<Token> tokenize(final String input) {
        List<Token> result = new ArrayList<>();
        if (input == null) {
            return result;
        }

        State state = State.WHITESPACE;
        char ch; // character in hand
        int tokenIndex = -1;
        StringBuilder token = new StringBuilder("");

        for (int i = 0; i < input.length(); i++) {
            ch = input.charAt(i);
            switch (state) {
                case WHITESPACE:
                    if (Character.isWhitespace(ch)) {
                        // keep state
                    } else if (Character.isLetterOrDigit(ch) || ch == '_') {
                        state = State.WORD;
                        tokenIndex = i;
                        token.append(ch);
                    } else if (ch == '"') {
                        state = State.STRINGDQ;
                        tokenIndex = i;
                    } else if (ch == '\'') {
                        state = State.STRINGSQ;
                        tokenIndex = i;
                    } else if (ch == '#') {
                        state = State.COMMENT;
                    } else {
                        state = State.WORD;
                        tokenIndex = i;
                        token.append(ch);
                    }
                    break;

                case WORD:
                    if (Character.isWhitespace(ch)) {
                        // submit token
                        result.add(new Token(tokenIndex, token.toString()));
                        token.setLength(0);
                        state = State.WHITESPACE;
                    } else if (ch == '#') {
                        // submit token
                        result.add(new Token(tokenIndex, token.toString()));
                        token.setLength(0);
                        state = State.COMMENT;
                    } else {
                        // for now we do allow special chars in words
                        token.append(ch);
                    }
                    break;

                case STRINGDQ:
                    if (ch == '"') {
                        if (i < input.length() - 1 && input.charAt(i + 1) == '"') {
                            token.append('"');
                            i++;
                            // and keep state
                        } else {
                            state = State.WORD;
                        }
                    } else {
                        token.append(ch);
                    }
                    break;

                case STRINGSQ:
                    if (ch == '\'') {
                        if (i < input.length() - 1 && input.charAt(i + 1) == '\'') {
                            token.append('\'');
                            i++;
                            // and keep state
                        } else {
                            state = State.WORD;
                        }
                    } else {
                        token.append(ch);
                    }
                    break;

                case COMMENT:
                    // eat ch
                    break;

                default:
                    assert false : "Unknown state in StringTokenizer state machine";
                    break;
            }
        }

        if (state == State.WORD || state == State.STRINGDQ || state == State.STRINGSQ) {
            result.add(new Token(tokenIndex, token.toString()));
        }

        return result;
    }
}
