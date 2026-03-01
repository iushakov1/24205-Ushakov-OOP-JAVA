package org.calc;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        StackCalculator calculator = new StackCalculator();

        if (args.length > 0) {
            try (InputStream is = new FileInputStream(args[0])) {
                logger.trace("trying to start in file mode");
                calculator.run(is);
            } catch (FileNotFoundException e) {
                logger.warn("cannot find the file: {}", args[0]);
                System.out.println("Cannot find the file: " + args[0]);
            } catch (IOException e) {
                logger.warn("cannot read the file: {}", e.getMessage());
                System.out.println("Cannot read the file");
            }
        } else {
            logger.debug("starting in console mode");
            System.out.println("Console mode start: ");
            calculator.run(System.in);
        }
    }
}