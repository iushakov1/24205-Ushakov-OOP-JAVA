package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

public class ConsoleUI implements GameUI{
    private static final Logger ui = LoggerFactory.getLogger("UserInterface");
    private static final Logger logger = LoggerFactory.getLogger(ConsoleUI.class);
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public int getUserIntInput(String message) {
        while (true) {
            ui.info("{}", message);
            String input = scanner.nextLine();
            logger.debug("User input an integer: {}", input);
            try {
                int value = Integer.parseInt(input);
                logger.debug("Successfully parsed integer: {}", value);
                return value;
            } catch (NumberFormatException e) {
                logger.warn("Failed to parse an integer from input: {}", input);
                ui.info("Error: write an integer number\n");
            }
        }
    }

    @Override
    public void showMessage(String message){
        ui.info("{}\n", message);
    }

    @Override
    public String getUserInput(){
        ui.info("> ");
        String input = scanner.nextLine().trim();
        logger.debug("read user input: {}", input);
        return input;
    }

    @Override
    public void showGuessResult(GuessResult result){
        logger.debug("Game result: Bulls={}, Cows={}", result.bulls(), result.cows());
        ui.info("Results: {} bulls, {} cows\n", result.bulls(), result.cows());
    }

    @Override
    public void showGameStatus(int attemptsLeft, long timeLeftSeconds){
        logger.debug("Status: {} attempts left, {}s remain", attemptsLeft, timeLeftSeconds);
        ui.info("[Attempts: {}]", attemptsLeft);
        if(timeLeftSeconds > 0){
            ui.info("[Time: {}s", timeLeftSeconds);
        }
        ui.info("\n");
    }
}
