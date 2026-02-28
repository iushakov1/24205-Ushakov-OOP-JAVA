package org.calc;

import org.calc.commands.Command;
import org.calc.exceptions.CalculatorException;

import java.io.InputStream;
import java.util.*;

public class StackCalculator {
    private final CommandFactory factory = new CommandFactory();
    private final ExecutionContext context = new ExecutionContext();
    //добавить логгер
    public void run(InputStream input){
        Scanner scanner = new Scanner(input);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine().trim();
            if(line.isEmpty()){
                continue;
            }

            String[] parts = line.split("\\s+");
            String cmdName = parts[0];
            List<String> args = new ArrayList<>();
            for(int i = 1; i < parts.length; ++i){
                args.add(parts[i]);
            }

            try{
                Command command = factory.createCommand(cmdName);
                if(command == null){
                    throw new CalculatorException("Unknown command: " + cmdName);
                }
                command.execute(context, args);
            }
            catch (CalculatorException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
