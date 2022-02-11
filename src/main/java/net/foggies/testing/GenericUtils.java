package net.foggies.testing;

public class GenericUtils {

    public static <T extends Number> T subtract(T x, T y) {
        if (x == null || y == null) {
            return null;
        }

        if (x instanceof Double) {
            return (T) new Double(x.doubleValue() - y.doubleValue());
        } else if (x instanceof Integer) {
            return (T) new Integer(x.intValue() - y.intValue());
        } else if (x instanceof Long) {
            return (T) new Long(x.longValue() - y.longValue());
        } else {
            throw new IllegalArgumentException("Type " + x.getClass() + " is not supported by this method");
        }
    }

    public static <T extends Number> T add(T x, T y) {

        if (x == null || y == null) {
            return null;
        }

        if (x instanceof Double) {
            return (T) new Double(x.doubleValue() + y.doubleValue());
        } else if (x instanceof Integer) {
            return (T) new Integer(x.intValue() + y.intValue());
        } else if (x instanceof Long) {
            return (T) new Long(x.longValue() + y.longValue());
        } else {
            throw new IllegalArgumentException("Type " + x.getClass() + " is not supported by this method");
        }
    }

}
