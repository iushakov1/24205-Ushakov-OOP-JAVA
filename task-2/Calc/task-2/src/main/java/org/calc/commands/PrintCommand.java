package org.calc.commands;

import org.calc.ExecutionContext;
import org.calc.exceptions.CalculatorException;
import org.calc.exceptions.StackUnderFlowException;

import java.util.List;

@CommandName("PRINT")
public class PrintCommand implements Command{
    @Override
    public void execute(ExecutionContext context, List<String> args) throws CalculatorException{
        try{
            Double el = context.getStack().peekFirst();
            if(el == null){
                throw new StackUnderFlowException();
            }
            System.out.println(el);
        }
        catch (StackUnderFlowException e){

        }
    }
}
