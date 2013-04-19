package org.lsst.ccs.command.demo;

import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.annotations.Parameter;

/**
 ***************************************************************************
 **
 ** Program to test the Java RCM register access routines
 *
 * * @author Owen Saxton *
 * **************************************************************************
 */
public class RCMReg {

    /**
     ***************************************************************************
     **
     ** Processes the CONNECT command *
     * **************************************************************************
     */
    @Command(description = "Connect to an RCM")
    public void connect(
            @Parameter(name = "id", description = "The id of the RCM") int id) {
    }

    /**
     ***************************************************************************
     **
     ** Processes the SHOW command *
     * **************************************************************************
     */
    @Command(description = "Show the current connection parameters")
    public void show() {
    }

    /**
     ***************************************************************************
     **
     ** Processes the READ command *
     * **************************************************************************
     */
    @Command(description = "Read and display registers from the RCM")
    public void read(
            @Parameter(name = "address", description = "Read and display registers from the RCM") int address,
            @Parameter(name = "count", description = "The number of registers to read (default 1)", defaultValue = "1") int count) {
    }

    /**
     ***************************************************************************
     **
     ** Processes the WRITE command *
     * **************************************************************************
     */
    @Command(description = "Write to registers on the RCM")
    private void write(
            @Parameter(name = "address", description = "The address of the first register to write") int address,
            @Parameter(name = "value", description = "The value(s) to write") int[] value)
    {
 
    }

}
