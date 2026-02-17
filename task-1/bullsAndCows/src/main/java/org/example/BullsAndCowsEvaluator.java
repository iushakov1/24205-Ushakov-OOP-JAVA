package org.example;

import java.util.Objects;

public class BullsAndCowsEvaluator {
    public GuessResult evaluate(String secret, String guess){
        Objects.requireNonNull(secret, "Secret cannot be null");
        Objects.requireNonNull(guess, "Guess cannot be null");

        if(secret.length() != guess.length()){
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

        return new GuessResult(bulls, cows, bulls == secret.length());
    }
}
