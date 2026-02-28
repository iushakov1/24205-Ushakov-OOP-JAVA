package org.calc.exceptions;

public class DivByZeroException extends CalculatorException {
    public DivByZeroException() {
        super("Cannot take a division by zero");
    }
}
