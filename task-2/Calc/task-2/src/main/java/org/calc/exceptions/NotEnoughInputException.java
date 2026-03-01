package org.calc.exceptions;

public class NotEnoughInputException extends CalculatorException {
    public NotEnoughInputException(){
        super("Not enough elements in input");
    }
}
