package org.calc;

import org.calc.commands.*;
import java.io.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandManager {
    private static final Logger logger = LoggerFactory.getLogger(CommandManager.class);
    private final Map<String, CommandCreator> creators = new HashMap<>();

    private static class ConcreteCommandCreator implements CommandCreator{
        private final Class<? extends Command> classToInstantiate;

        public ConcreteCommandCreator(Class<? extends Command> cls){
            this.classToInstantiate = cls;
        }

        @Override
        public Command create(){
            try{
                return classToInstantiate.getDeclaredConstructor().newInstance();
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    public CommandManager(){
        try(InputStream is = CommandManager.class.getResourceAsStream("commands.cfg")){
            if(is == null){
                throw new RuntimeException("<commands.cfg> is broken. Check resource folder");
            }

            Scanner sc = new Scanner(is);
            while (sc.hasNextLine()){
                String className = sc.nextLine().trim();
                Class<?> cls = Class.forName(className);
                if(cls.isAnnotationPresent(CommandName.class)){
                    String name = cls.getAnnotation(CommandName.class).value();
                    logger.info("found command: {}", name);
                    creators.put(name, new ConcreteCommandCreator((Class<? extends Command>)cls));
                }
            }
        }
        catch(IOException e){
            throw new RuntimeException("Failed to read commands.cfg", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Command createCommand(String name){
        CommandCreator creator = creators.get(name);
        if(creator == null){
            logger.warn("command creator got null at name: {}", name);
            return null;
        }
        return creator.create();
    }
}
