package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

public class BullsAndCowsEvaluator {
    private static final Logger logger = LoggerFactory.getLogger(BullsAndCowsEvaluator.class);
    public GuessResult evaluate(String secret, String guess){
        Objects.requireNonNull(secret, "Secret cannot be null");
        Objects.requireNonNull(guess, "Guess cannot be null");
        if(secret.length() != guess.length()){
            logger.warn("incorrect length of <guess> and <secret>: guessLen={}, secretLen={}", guess.length(), secret.length());
            throw new IllegalArgumentException("Length mismatch");
        }

        int bulls = 0;
        int cows = 0;

        boolean[] secretUsed = new boolean[secret.length()];
        boolean[] guessUsed = new boolean[guess.length()];

        for(int i = 0; i < secret.length(); ++i){
            if (secret.charAt(i) == guess.charAt(i)) {
                ++bulls;
                secretUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        for(int i = 0; i < secret.length(); ++i){
            if(guessUsed[i]) continue;

            for(int j = 0; j < secret.length(); ++j){
                if(!secretUsed[j] && guess.charAt(i) == secret.charAt(j)){
                    ++cows;
                    secretUsed[j] = true;
                    break;
                }
            }
        }
        logger.info("returning result: {} <bulls>, {} cows, user is win = {}", bulls, cows, bulls == secret.length());
        return new GuessResult(bulls, cows, bulls == secret.length());
    }
}
