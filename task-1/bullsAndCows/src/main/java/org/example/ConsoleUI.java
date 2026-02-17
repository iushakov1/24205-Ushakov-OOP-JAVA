package org.example;

import java.util.Scanner;

public class ConsoleUI implements GameUI{
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public int getUserIntInput(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: write integer number");
            }
        }
    }

    @Override
    public void showMessage(String message){
        System.out.println(message);
    }

    @Override
    public String getUserInput(){
        System.out.print("> ");
        return scanner.nextLine().trim();
    }

    @Override
    public void showGuessResult(GuessResult result){
        System.out.printf("Results: %d bulls, %d cows\n", result.bulls(), result.cows());
    }

    @Override
    public void showGameStatus(int attemptsLeft, long timeLeftSeconds){
        System.out.print("[Attempts: " + attemptsLeft + "]");
        if(timeLeftSeconds > 0){
            System.out.print("[Time: " + timeLeftSeconds + "c]");
        }
        System.out.println();
    }
}
