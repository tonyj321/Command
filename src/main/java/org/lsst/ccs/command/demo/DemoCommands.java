package org.lsst.ccs.command.demo;

import org.lsst.ccs.command.annotations.Command;
import org.lsst.ccs.command.annotations.Parameter;

/**
 * A class designed to illustrate how a class can be annotated to accept
 * commands. Note that the only dependencies this class has is on the
 * annotations package.
 *
 * @author The CCS Team
 */
public class DemoCommands {

    @Command(description = "Get the temperature of a module")
    public double getTemperature(@Parameter(name = "module", description = "module (0-9)") int module) {
        if (module<0 || module>9) throw new IllegalArgumentException("module");
        return Math.random();
    }
}
