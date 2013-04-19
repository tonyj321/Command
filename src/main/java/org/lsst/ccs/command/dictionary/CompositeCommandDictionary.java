package org.lsst.ccs.command.dictionary;

import java.util.Iterator;

/**
 * A command dictionary to which other command dictionaries can be added and 
 * removed.
 * @author tonyj
 */
public class CompositeCommandDictionary implements CommandDictionary {
    //FIXME: Not yet implemented
    @Override
    public Iterator<CommandDefinition> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
