package org.commands;

import org.calc.commands.*;
import org.calc.exceptions.*;

import java.util.List;
import java.lang.Math;

@CommandName("SQRT")
public class SqrtCommand implements Command{
    @Override
    public void execute(ExecutionContext context, List<String> args) throws CalculatorException {
        if(context.getStack().isEmpty()){
            throw new StackUnderFlowException();
        }

        Double a = context.getStack().pop();
        if(a < 0){
            context.getStack().push(a);
            throw new SqrtOfNegativValException();
        }

        context.getStack().push(Math.sqrt(a));
    }
}
