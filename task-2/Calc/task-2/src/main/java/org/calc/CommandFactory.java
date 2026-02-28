package org.calc;

import org.calc.commands.*;
import java.io.*;
import java.util.*;

public class CommandFactory {

    private final Map<String, Class<? extends Command>> commands = new HashMap<>();

    public CommandFactory(){
        try(InputStream is = CommandFactory.class.getResourceAsStream("commands.cfg")){
            Scanner sc = new Scanner(is);
            while (sc.hasNextLine()){
                String className = sc.nextLine().trim();
                Class<?> cls = Class.forName(className);
                if(cls.isAnnotationPresent(CommandName.class)){
                    commands.put(cls.getAnnotation(CommandName.class).value(), (Class<? extends Command>) cls);
                }
            }
        }
        catch(Exception e){
            //поставить логгер
        }
    }

    public Command createCommand(String name){
        try{
            Class<? extends Command> cls = commands.get(name);
            if(cls != null){
                return cls.getDeclaredConstructor().newInstance();
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }
}
