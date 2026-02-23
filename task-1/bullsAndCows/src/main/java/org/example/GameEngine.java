package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameEngine {
    private final GameSettings settings;
    private final SecretGenerator generator;
    private final BullsAndCowsEvaluator evaluator;
    private final GameUI ui;

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    public GameEngine(GameSettings settings, SecretGenerator generator,
                      BullsAndCowsEvaluator evaluator, GameUI ui){
        this.settings = settings;
        this.generator = generator;
        this.evaluator = evaluator;
        this.ui = ui;
        logger.debug("successful created game engine");
    }

    public void run(){

        String secret = generator.generate(settings.sequenceLength());
        logger.debug("generated new secret: {}", secret);
        int attemptsLeft = settings.maxAttempts();
        boolean isWon = false;

        logger.info("starting game");
        ui.showMessage("Game has been start! I wished number of " + settings.sequenceLength() + " uniq digits");
        InputValidator inputValidator = new InputValidator();
        while(attemptsLeft > 0 && !isWon){
            ui.showGameStatus(attemptsLeft, settings.timeLimitSeconds());

            long startTime = System.currentTimeMillis();
            String guess = ui.getUserInput();
            long endTime = System.currentTimeMillis();

            if(settings.isTimeLimitEnabled() && ( (endTime - startTime) > settings.timeLimitSeconds()*1000) ){
                logger.info("time is run out: {} > {}", (endTime - startTime), settings.timeLimitSeconds()*1000);
                ui.showMessage("too slow! Attempt is lost");
                --attemptsLeft;
                continue;
            }

            try{
                inputValidator.validate(guess, settings.sequenceLength());
                GuessResult result = evaluator.evaluate(secret, guess);
                ui.showGuessResult(result);
                logger.debug("showed guess result");
                if(result.isWin()){
                    isWon = true;
                }
            }
            catch (IllegalArgumentException e){
                logger.warn("catch an invalid <guess> from user: {}", guess);
                ui.showMessage("Error: " + e.getMessage());
                continue;
            }
            --attemptsLeft;
        }

        if(isWon){
            logger.info("user won");
            ui.showMessage("Congratulations! You won");
        }
        else{
            logger.info("user lost");
            ui.showMessage("Game over. No attempts left. Was wished: " + secret);
        }
    }
}
