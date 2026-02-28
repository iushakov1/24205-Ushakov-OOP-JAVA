package org.calc.commands;

import org.calc.ExecutionContext;
import org.calc.exceptions.CalculatorException;

import java.util.List;

public interface Command {
    void execute(ExecutionContext context, List<String> args) throws CalculatorException;
}
