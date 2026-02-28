package org.calc.exceptions;

public class SqrtOfNegativValException extends CalculatorException {
    public SqrtOfNegativValException() {
        super("Cannot take a square of a negative value");
    }
}
