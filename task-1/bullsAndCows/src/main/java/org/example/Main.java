package org.example;

public class Main {
    public static void main(String[] args) {
        GameUI ui = new ConsoleUI();
        ui.showMessage("welcome to the BULLS&COWS");

        int length = ui.getUserIntInput("choose length of secret number (1-10): ");
        int attempts = ui.getUserIntInput("choose number of attempts: ");
        long time = ui.getUserIntInput("if you want to play with timer, write any number > 0\n" +
                "if not, write 0: ");
        GameSettings settings;
        while(true){
            try{
                settings = new GameSettings(length, attempts, time, (time>0));
                break;
            }
            catch (IllegalArgumentException e){
                ui.showMessage("incorrect settings: " + e.getMessage());
                ui.showMessage("please, try again");
                length = ui.getUserIntInput("choose length of secret number (1-10): ");
                attempts = ui.getUserIntInput("choose number of attempts: ");
                time = ui.getUserIntInput("if you want to play with timer, write any number > 0\n" +
                        "if not, write 0: ");
            }
        }


        SecretGenerator generator = new RandomSecretGenerator();
        BullsAndCowsEvaluator evaluator = new BullsAndCowsEvaluator();


        GameEngine engine = new GameEngine(
                settings,
                generator,
                evaluator,
                ui
        );

        try{
            engine.run();
        }
        catch (Exception e){
            ui.showMessage("Error happend: " + e.getMessage());
        }
    }
}