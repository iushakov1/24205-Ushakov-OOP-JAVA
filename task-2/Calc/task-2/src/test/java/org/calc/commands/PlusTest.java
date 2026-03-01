package org.calc.commands;

import org.calc.StackCalculator;
import org.calc.exceptions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlusTest {
    private final StackCalculator calculator = new StackCalculator();

    @Test
    public void SimplePlus() throws CalculatorException{
        calculator.executeCommand("push 50");
        calculator.executeCommand("push 2");
        calculator.executeCommand("plus");
        assertEquals(52, calculator.getContext().getStack().peekFirst());
    }
}
