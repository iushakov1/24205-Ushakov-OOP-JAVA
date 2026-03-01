package org.calc.commands;

import org.calc.StackCalculator;
import org.calc.exceptions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PushTest {
    private final StackCalculator calculator = new StackCalculator();

    @Test
    public void SimplePush() throws CalculatorException {
        calculator.executeCommand("PUSH 1");
        assertEquals(1, calculator.getContext().getStack().peekFirst());
    }

    @Test void NotValidPush() throws CalculatorException{
        assertThrows(InvalidArgumentException.class, ()->{calculator.executeCommand("PUSH wrong");});
    }
}
