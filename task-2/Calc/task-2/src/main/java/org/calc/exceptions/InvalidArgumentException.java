package org.calc.exceptions;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String arg) {
        super("given invalid argument: " + arg);
    }
}
