package org.calc.commands;

import org.calc.StackCalculator;
import org.calc.exceptions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class SqrtTest {
    private final StackCalculator calculator = new StackCalculator();
    @Test
    public void SimpleSquare() throws CalculatorException{
        calculator.executeCommand("PUSH 4");
        calculator.executeCommand("SQRT");
        assertEquals(2, calculator.getContext().getStack().peekFirst());
    }
    @Test
    public void NegativeNum()throws CalculatorException{
        calculator.executeCommand("PUSH -1");
        assertThrows(SqrtOfNegativValException.class, ()->{calculator.executeCommand("SQRT");});
    }
}
