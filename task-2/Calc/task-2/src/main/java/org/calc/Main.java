package org.calc;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        StackCalculator calculator = new StackCalculator();

        if (args.length > 0) {
            try (InputStream is = new FileInputStream(args[0])) {
                calculator.run(is);
            } catch (FileNotFoundException e) {
                System.out.println("Cannot find the file: " + args[0]);
            } catch (IOException e) {
                System.out.println("Cannot read the file: " + e.getMessage());
            }
        } else {
            System.out.println("Console mode start: ");
            calculator.run(System.in);
        }
    }
}