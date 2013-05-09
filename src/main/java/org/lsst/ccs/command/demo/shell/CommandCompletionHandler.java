package org.lsst.ccs.command.demo.shell;

import java.io.IOException;
import java.util.List;
import jline.console.ConsoleReader;
import jline.console.completer.CandidateListCompletionHandler;

/**
 * Extend JLine's built-in completion handler to handle giving
 * information on a specific parameters. 
 * @author tonyj
 */
class CommandCompletionHandler extends CandidateListCompletionHandler {

    @Override
    public boolean complete(ConsoleReader reader, List<CharSequence> candidates, int pos) throws IOException {
        //FIXME: Using # to indicate parameter help is ugly
        if (candidates.size() == 1 && candidates.get(0).length()>1 && candidates.get(0).charAt(0)=='#') {
            reader.println();
            reader.println(candidates.get(0).subSequence(1, candidates.get(0).length()));
            reader.drawLine();
            return true;
        } else {
            return super.complete(reader, candidates, pos);
        }
    }

    
}
