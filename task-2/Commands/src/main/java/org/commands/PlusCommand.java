package org.commands;

import org.calc.commands.*;
import org.calc.exceptions.*;

import java.util.List;

@CommandName("PLUS")
public class PlusCommand implements Command{
    @Override
    public void execute(ExecutionContext context, List<String> args) throws CalculatorException{
        if(context.getStack().size() < 2){
            throw new StackUnderFlowException();
        }

        Double b = context.getStack().pop();
        Double a = context.getStack().pop();

        context.getStack().push(a + b);
    }
}
