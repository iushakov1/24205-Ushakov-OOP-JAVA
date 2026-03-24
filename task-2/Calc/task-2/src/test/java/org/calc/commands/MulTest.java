package org.calc.commands;

import org.calc.core.StackCalculator;
import org.calc.exceptions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MulTest {
    private final StackCalculator calculator = new StackCalculator();

    @Test
    public void simpleMul() throws CalculatorException{
        calculator.executeCommand("push 50");
        calculator.executeCommand("push 2");
        calculator.executeCommand("mul");
        assertEquals(100, calculator.getContext().getStack().peekFirst());
    }
}
