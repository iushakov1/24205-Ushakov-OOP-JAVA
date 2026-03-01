package org.calc.commands;

import org.calc.StackCalculator;
import org.calc.exceptions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinusTest {
    private final StackCalculator calculator = new StackCalculator();

    @Test
    public void simpleMinus() throws CalculatorException {
        calculator.executeCommand("push 50");
        calculator.executeCommand("push 2");
        calculator.executeCommand("minus");
        assertEquals(48, calculator.getContext().getStack().peekFirst());
    }
}
