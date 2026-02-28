package org.calc.commands;

import org.calc.ExecutionContext;
import org.calc.exceptions.CalculatorException;

import java.util.List;

@CommandName("PUSH")
public class PushCommand implements Command {
    @Override
    public void execute(ExecutionContext context, List<String> args) throws CalculatorException{
        Double value;
        String arg = args.get(0);
        if(context.getParameters().containsKey(arg)){
            value = context.getParameters().get(arg);
        }
        else{
            try{
                value = Double.parseDouble(arg);
            }
            catch (NumberFormatException e){
                throw new CalculatorException("Unknown parameter or number: " + arg);
            }
        }
        context.getStack().push(value);
    }
}
