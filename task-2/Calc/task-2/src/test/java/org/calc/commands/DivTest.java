package org.calc.commands;

import org.calc.StackCalculator;
import org.calc.exceptions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DivTest {
    private final StackCalculator calculator = new StackCalculator();
    @Test
    public void simpleDiv() throws CalculatorException{
        calculator.executeCommand("push 50");
        calculator.executeCommand("push 2");
        calculator.executeCommand("div");
        assertEquals(25, calculator.getContext().getStack().peekFirst());
    }

    @Test
    public void zeroDiv() throws CalculatorException{
        calculator.executeCommand("push 50");
        calculator.executeCommand("push 0");
        assertThrows(DivByZeroException.class, () -> {calculator.executeCommand("div");} );
    }
}
