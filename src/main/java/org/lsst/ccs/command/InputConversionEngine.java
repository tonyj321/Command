package org.lsst.ccs.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class is responsible for converting strings to objects of a particular
 * class. It is used to convert command arguments into objects to be passed to
 * the corresponding method. The current implementation is very simple, dealing
 * only with built in types, or types with constructors which take a string. It
 * would be possible to add back in the cliche functionality which allowed extra
 * input converters to be registered with the input conversion engine.
 *
 * Note that in a distributed system argument conversion is only done on the
 * remote (server) end, so any special classes used for arguments only need to
 * be present on the remote (server). In CCS terminology this means that the
 * classes only need to be present in the subsystem.
 *
 * @author tonyj
 */
class InputConversionEngine {

    Object convertArgToType(String arg, Class type) throws CommandInvocationException {
        try {
            if (type.equals(String.class) || type.isInstance(arg)) {
                return arg;
            } else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
                return Integer.parseInt(arg);
            } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
                return Long.parseLong(arg);
            } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
                return Double.parseDouble(arg);
            } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
                return Float.parseFloat(arg);
            } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
                // Boolean.parseBoolean treats anything it does not recognize as true, which does not 
                // see appropriate here, so instead
                if ("true".equalsIgnoreCase(arg)) return Boolean.TRUE;
                if ("false".equalsIgnoreCase(arg)) return Boolean.FALSE;
                throw new CommandInvocationException("Can't convert %s to Boolean",arg);
            } else if (type.isEnum()) {
                return Enum.valueOf(type, arg);
            } else {
                Constructor c = type.getConstructor(String.class);
                return c.newInstance(arg);

            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new CommandInvocationException("Error instantiating class %s using string %s", ex, type.getName(), arg);
        } catch (IllegalArgumentException | NoSuchMethodException e) {
            throw new CommandInvocationException("Can't convert string %s to class %s", e, arg, type.getName());
        }
    }
}