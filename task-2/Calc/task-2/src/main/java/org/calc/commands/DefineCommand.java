package org.calc.commands;

import org.calc.ExecutionContext;
import org.calc.exceptions.CalculatorException;
import org.calc.exceptions.StackUnderFlowException;

import java.util.List;

@CommandName("DEFINE")
public class DefineCommand implements Command{
    @Override
    public void execute(ExecutionContext context, List<String> args) throws CalculatorException{
        if(args.size() < 2){
            throw new StackUnderFlowException();
        }

        String parName = args.get(0);
        try{
            Double par = Double.parseDouble(args.get(1));
            context.getParameters().put(parName, par);
        }
        catch (NumberFormatException e){
            throw new CalculatorException("Value of parameter must be number: " + args.get(1));
        }
    }
}
