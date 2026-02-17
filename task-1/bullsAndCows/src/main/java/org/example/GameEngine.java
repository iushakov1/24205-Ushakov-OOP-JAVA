package org.example;

public class GameEngine {
    private final GameSettings settings;
    private final SecretGenerator generator;
    private final BullsAndCowsEvaluator evaluator;
    private final GameUI ui;

    public GameEngine(GameSettings settings, SecretGenerator generator,
                      BullsAndCowsEvaluator evaluator, GameUI ui){
        this.settings = settings;
        this.generator = generator;
        this.evaluator = evaluator;
        this.ui = ui;
    }

    public void run(){
        String secret = generator.generate(settings.sequenceLength());
        int attemptsLeft = settings.maxAttempts();
        boolean isWon = false;

        ui.showMessage("Game has been start! I wished number of " + settings.sequenceLength() + " uniq digits");
        InputValidator inputValidator = new InputValidator();
        while(attemptsLeft > 0 && !isWon){
            ui.showGameStatus(attemptsLeft, settings.timeLimitSeconds());

            long startTime = System.currentTimeMillis();
            String guess = ui.getUserInput();
            long endTime = System.currentTimeMillis();

            if(settings.isTimeLimitEnabled() && ( (endTime - startTime) > settings.timeLimitSeconds()*1000) ){
                ui.showMessage("too slow! Attempt is lost");
                --attemptsLeft;
                continue;
            }

            try{
                inputValidator.validate(guess, settings.sequenceLength());
                GuessResult result = evaluator.evaluate(secret, guess);
                ui.showGuessResult(result);

                if(result.isWin()){
                    isWon = true;
                }
            }
            catch (IllegalArgumentException e){
                ui.showMessage("Error: " + e.getMessage());
                continue;
            }
            --attemptsLeft;
        }

        if(isWon){
            ui.showMessage("Congratulations! You won");
        }
        else{
            ui.showMessage("Game over. No attempts left. Was wished: " + secret);
        }
    }
}
