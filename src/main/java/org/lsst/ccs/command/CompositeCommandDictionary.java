package org.lsst.ccs.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * A command dictionary to which other command dictionaries can be added and
 * removed.
 *
 * @author tonyj
 */
class CompositeCommandDictionary implements Dictionary {

    LinkedHashSet<Dictionary> dicts = new LinkedHashSet<>();

    void add(Dictionary commandDictionary) {
        dicts.add(commandDictionary);
    }

    void remove(Dictionary commandDictionary) {
        dicts.remove(commandDictionary);
    }

    @Override
    public boolean containsCommand(TokenizedCommand tc) {
        for (Dictionary dict : dicts) {
            if (dict.containsCommand(tc)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int findCommand(TokenizedCommand tc) {
        int offset = 0;
        for (Dictionary dict : dicts) {
            if (dict.containsCommand(tc)) {
                return offset + dict.findCommand(tc);
            }
            offset += dict.size();
        }
        return -1;
    }
    

    @Override
    public Iterator<DictionaryCommand> iterator() {
        // Brute force implementation, could do better
        ArrayList<DictionaryCommand> allCommands = new ArrayList<>();
        for (Dictionary dict : dicts) {
            for (DictionaryCommand def : dict) {
                allCommands.add(def);
            }
        }
        return allCommands.iterator();
    }

    @Override
    public int size() {
        int result = 0;
        for (Dictionary dict : dicts) {
            result += dict.size();
        }
        return result;
    }

    @Override
    public DictionaryCommand get(int index) {
        for (Dictionary dict : dicts) {
            if (index<dict.size()) return dict.get(index);
            index -= dict.size();
        }
        throw new IndexOutOfBoundsException("index="+index);
    }
}
