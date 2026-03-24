package org.commands;

import org.calc.commands.*;
import org.calc.exceptions.*;

import java.util.List;

@CommandName("POP")
public class PopCommand implements Command {
    @Override
    public void execute(ExecutionContext context, List<String> args) throws CalculatorException{
        if(context.getStack().peekFirst() == null){
            throw new StackUnderFlowException();
        }
        System.out.println(context.getStack().pop());
    }
}
