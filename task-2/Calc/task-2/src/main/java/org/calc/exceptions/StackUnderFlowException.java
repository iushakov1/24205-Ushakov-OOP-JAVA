package org.calc.exceptions;

public class StackUnderFlowException extends CalculatorException {
    public StackUnderFlowException(){
        super("not enough elements in stack");
    }
}
