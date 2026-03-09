package org.commands;

import org.calc.commands.*;
import org.calc.exceptions.*;

import java.util.List;

@CommandName("PRINT")
public class PrintCommand implements Command{
    @Override
    public void execute(ExecutionContext context, List<String> args) throws CalculatorException{
        Double el = context.getStack().peekFirst();
        if(el == null){
            throw new StackUnderFlowException();
        }
        System.out.println(el);
    }
}
