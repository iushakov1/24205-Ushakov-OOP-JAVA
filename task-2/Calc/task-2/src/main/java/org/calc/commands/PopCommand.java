package org.calc.commands;

import org.calc.ExecutionContext;
import org.calc.exceptions.CalculatorException;
import org.calc.exceptions.StackUnderFlowException;

import java.util.List;

@CommandName("POP")
public class PopCommand implements Command {
    @Override
    public void execute(ExecutionContext context, List<String> args) throws CalculatorException{
        try {
            if(context.getStack().peekFirst() == null){
                throw new StackUnderFlowException();
            }
            System.out.println(context.getStack().pop());
        }
        catch (StackUnderFlowException e){

        }
    }
}
