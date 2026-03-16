package org.calc.core;

import org.calc.commands.*;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
                String line = sc.nextLine().trim();
                if(line.isEmpty()){
                    continue;
                }
                String[] parts = line.split(":");

                if(parts.length == 1){
                    String jarPath = parts[0].trim();
                    File file = new File(jarPath);
                    URL[] urls = {file.toURI().toURL()};
                    try(JarFile jarFile = new JarFile(jarPath);
                    URLClassLoader loader = new URLClassLoader(urls, this.getClass().getClassLoader());)
                    {
                        Enumeration<JarEntry> entries = jarFile.entries();

                        while(entries.hasMoreElements()){
                            JarEntry entry = entries.nextElement();
                            if(entry.getName().endsWith(".class")){
                                String className = entry.getName()
                                        .replace("/", ".")
                                        .replace(".class", "");
                                Class<?> cls = Class.forName(className, true, loader);
                                if(cls.isAnnotationPresent(CommandName.class)){
                                    String name = cls.getAnnotation(CommandName.class).value();
                                    creators.put(name, new ConcreteCommandCreator((Class<? extends Command>) cls));
                                }
                            }
                        }
                        logger.info("Successfully loaded commands from {}", jarPath);
                    }
                    catch (IOException e){
                        logger.warn("I/O error when read jar: {}", jarPath);
                    }
                    catch (SecurityException e){
                        logger.warn("The jar is secured: {}", jarPath);
                    }
                    catch (Exception e){
                        logger.warn("Error when reading archive: {}", jarPath);
                    }
                }
                else if(parts.length == 2){
                    String jarPath = parts[0].trim();
                    String className = parts[1].trim();

                    try{
                        File jarFile = new File(jarPath);
                        URL jarUrl = jarFile.toURI().toURL();

                        URLClassLoader loader = new URLClassLoader(
                            new URL[]{jarUrl},
                            this.getClass().getClassLoader()
                        );

                        Class<?> cls = Class.forName(className, true, loader);

                        if(cls.isAnnotationPresent(CommandName.class)){
                            String name = cls.getAnnotation(CommandName.class).value();
                            logger.info("Successfully loaded command: {} from {}", name, jarPath);
                            creators.put(name, new ConcreteCommandCreator((Class<? extends Command>) cls));
                        }
                    } catch (Exception e){
                        logger.error("Failed to load class {} from {}: {}", className, jarPath, e.getMessage());
                    }
                }
                else{
                    logger.warn("incorrect config line: {}", line);
                }
            }
        }
        catch(IOException e){
            throw new RuntimeException("Failed to read commands.cfg", e);
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
