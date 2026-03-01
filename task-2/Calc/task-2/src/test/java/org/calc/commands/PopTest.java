package org.calc.commands;

import org.calc.StackCalculator;
import org.calc.exceptions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PopTest {
    private final StackCalculator calculator = new StackCalculator();

    @Test
    public void SimplePop() throws CalculatorException{
        calculator.executeCommand("PUSH 3");
        calculator.executeCommand("POP");
        assertEquals(0, calculator.getContext().getStack().size());
    }

    @Test
    public void NothingToPop() throws CalculatorException{
        assertThrows(StackUnderFlowException.class, () -> {calculator.executeCommand("POP");});
    }
}
