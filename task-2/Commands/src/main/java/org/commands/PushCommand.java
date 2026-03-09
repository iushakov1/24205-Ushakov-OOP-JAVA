package org.commands;

import org.calc.commands.*;
import org.calc.exceptions.*;

import java.util.List;

@CommandName("PUSH")
public class PushCommand implements Command {
    @Override
    public void execute(ExecutionContext context, List<String> args) throws CalculatorException{
        Double value;
        if(args.isEmpty()){
            throw new NotEnoughInputException();
        }
        String arg = args.get(0);
        if(context.getParameters().containsKey(arg)){
            value = context.getParameters().get(arg);
        }
        else{
            try{
                value = Double.parseDouble(arg);
            }
            catch (NumberFormatException e){
                throw new InvalidArgumentException(arg);
            }
        }
        context.getStack().push(value);
    }
}
