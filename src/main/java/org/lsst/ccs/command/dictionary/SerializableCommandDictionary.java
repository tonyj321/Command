package org.lsst.ccs.command.dictionary;

import java.util.ArrayList;

/**
 * A serializable command dictionary represents a set of commands extracted 
 * from the annotations on a single class. It is serializable to enable it
 * to be sent across a transport protocol in the event that the commands are
 * issued in a different virtual machine to the one where the commands are invoked.
 * @author The CCS Team
 */
public class SerializableCommandDictionary extends ArrayList<CommandDefinition> implements CommandDictionary {
    
}
