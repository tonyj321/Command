package org.lsst.ccs.command.toyconsole;

import java.io.IOException;
import jline.console.ConsoleReader;
import org.lsst.ccs.command.cliche.InputConversionEngine;
import org.lsst.ccs.command.dictionary.CommandInvoker;
import org.lsst.ccs.command.dictionary.SerializableCommandDictionary;

/**
 *
 * @author tonyj
 */
public class JLineShell {
    private final SerializableCommandDictionary dict;
    private final CommandInvoker invoker = new CommandInvoker(new InputConversionEngine());
    
    JLineShell(SerializableCommandDictionary dict) {
        this.dict = dict;
    }
    public static void run() throws IOException {
        ConsoleReader reader = new ConsoleReader();
        for (;;) {
            String line = reader.readLine(">>>");
            // FIXME: Something should happen here
            //Object result = invoker.invoke(dict)
        }        
    }
    public static void main(String[] args) throws IOException {
        SerializableCommandDictionary dict = new SerializableCommandDictionary();
        JLineShell shell = new JLineShell(dict);
        shell.run();
    }
}
