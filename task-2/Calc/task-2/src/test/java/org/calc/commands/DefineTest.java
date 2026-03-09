package org.calc.commands;

import org.calc.core.StackCalculator;
import org.calc.exceptions.CalculatorException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefineTest {
    private final StackCalculator calculator = new StackCalculator();

    @Test
    public void simpleDefine() throws CalculatorException {
        String input = "DEFINE a 4";
        calculator.executeCommand(input);
        assertEquals(4, calculator.getContext().getParameters().get("a"));
    }

    @Test
    public void badDefine() throws CalculatorException{
        String input = "DEFINE a b";
        assertThrows(CalculatorException.class, () -> {calculator.executeCommand(input);});
    }
}
