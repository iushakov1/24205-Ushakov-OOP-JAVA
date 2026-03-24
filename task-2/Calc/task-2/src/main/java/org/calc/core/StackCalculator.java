package org.calc.core;

import org.calc.commands.ExecutionContext;
import org.calc.commands.Command;
import org.calc.exceptions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

public class StackCalculator {
    private final CommandManager manager = new CommandManager();
    private final ExecutionContext context = new ExecutionContext();
    private static final Logger logger = LoggerFactory.getLogger(StackCalculator.class);

    public void executeCommand(String line) throws CalculatorException{
        if(line == null || line.trim().isEmpty()){
            logger.trace("Empty or null line given on execute");
            return;
        }
        String[] parts = line.trim().split("\\s+");
        String cmdName = parts[0].toUpperCase();

        List<String> args = new ArrayList<>();
        for(int i = 1; i < parts.length; ++i){
            args.add(parts[i]);
        }
        logger.debug("Got next args: {}", args);

        Command command = manager.createCommand(cmdName);
        if(command == null){
            throw new CalculatorException("Unknown command: " + cmdName);
        }
        command.execute(context, args);
    }

    public void run(InputStream input){
        Scanner scanner = new Scanner(input);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine().trim();
            if(line.isEmpty()){
                continue;
            }
            if(line.equalsIgnoreCase("exit")){
                logger.debug("finished working");
                return;
            }

            String[] parts = line.split("\\s+");
            String cmdName = parts[0].toUpperCase();
            List<String> args = new ArrayList<String>();
            for(int i = 1; i < parts.length; ++i){
                args.add(parts[i]);
            }
            logger.debug("Got next line: {}", args);

            try{
                Command command = manager.createCommand(cmdName);
                if(command == null){
                    throw new CalculatorException("Unknown command: " + cmdName);
                }
                logger.debug("Now executing: {}", cmdName);
                command.execute(context, args);
            }
            catch (CalculatorException e){
                logger.warn("Error while creating command: {}", e.getMessage());
                System.out.println("Failed to execute command: " + cmdName);
            }
        }
    }
    
    public ExecutionContext getContext(){
        return context;
    }
}
