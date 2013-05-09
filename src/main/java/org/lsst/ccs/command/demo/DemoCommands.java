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
        if (module < 0 || module > 9) {
            throw new IllegalArgumentException("module < 0 or > 9");
        }
        return Math.random();
    }

    @Command(description = "Add two arguments")
    public double add(double a, double b) {
        return a + b;
    }

    @Command(description = "Add any number of arguments")
    public double addAll(double... numbers) {
        double result = 0;
        for (double d : numbers) {
            result += d;
        }
        return result;
    }
    
    public enum Day {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
        THURSDAY, FRIDAY, SATURDAY 
    }
    @Command(description = "Convert day of week to an ordinal")
    public int dayOfWeek(@Parameter(name="Day") Day day) {
        return day.ordinal();
    }
    
    @Command(description = "Always generates an error")
    public void error() {
        throw new UnsupportedOperationException("This command not allowed");
    }
    
}
