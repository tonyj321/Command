package org.lsst.ccs.command.dictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * A command dictionary to which other command dictionaries can be added and
 * removed.
 *
 * @author tonyj
 */
class CompositeCommandDictionary implements CommandDictionary {

    LinkedHashSet<CommandDictionary> dicts = new LinkedHashSet<>();

    void add(CommandDictionary commandDictionary) {
        dicts.add(commandDictionary);
    }

    void remove(CommandDictionary commandDictionary) {
        dicts.remove(commandDictionary);
    }

    @Override
    public boolean containsCommand(TokenizedCommand tc) {
        for (CommandDictionary dict : dicts) {
            if (dict.containsCommand(tc)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int findCommand(TokenizedCommand tc) {
        int offset = 0;
        for (CommandDictionary dict : dicts) {
            if (dict.containsCommand(tc)) {
                return offset + dict.findCommand(tc);
            }
            offset += dict.size();
        }
        return -1;
    }
    

    @Override
    public Iterator<CommandDefinition> iterator() {
        // Brute force implementation, could do better
        ArrayList<CommandDefinition> allCommands = new ArrayList<>();
        for (CommandDictionary dict : dicts) {
            for (CommandDefinition def : dict) {
                allCommands.add(def);
            }
        }
        return allCommands.iterator();
    }

    @Override
    public int size() {
        int result = 0;
        for (CommandDictionary dict : dicts) {
            result += dict.size();
        }
        return result;
    }

    @Override
    public CommandDefinition get(int index) {
        for (CommandDictionary dict : dicts) {
            if (index<dict.size()) return dict.get(index);
            index -= dict.size();
        }
        throw new IndexOutOfBoundsException("index="+index);
    }
}
