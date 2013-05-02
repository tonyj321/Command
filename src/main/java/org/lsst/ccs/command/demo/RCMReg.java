package org.lsst.ccs.command.demo;

import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.annotations.Parameter;

/**
 ** Demo to test the Java RCM register access routines. Based on the class
 * in the rcm driver.
 * * @author Owen Saxton
 */
public class RCMReg {

    @Command(description = "Connect to an RCM")
    public void connect(
            @Parameter(name = "id", description = "The id of the RCM") int id) {
    }

    @Command(description = "Show the current connection parameters")
    public void show() {
    }

    @Command(description = "Read and display registers from the RCM")
    public void read(
            @Parameter(name = "address", description = "Read and display registers from the RCM") int address,
            @Parameter(name = "count", description = "The number of registers to read (default 1)", defaultValue = "1") int count) {
    }


    @Command(description = "Write to registers on the RCM")
    private void write(
            @Parameter(name = "address", description = "The address of the first register to write") int address,
            @Parameter(name = "value", description = "The value(s) to write") int[] value) {
    }
}
