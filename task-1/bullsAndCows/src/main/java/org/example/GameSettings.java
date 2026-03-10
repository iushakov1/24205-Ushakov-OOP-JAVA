package org.example;

public record GameSettings(int sequenceLength,
                           int maxAttempts,
                           long timeLimitSeconds,
                           boolean isTimeLimitEnabled) {
    public GameSettings{
        if(sequenceLength < 1 || sequenceLength > 10){
            throw new IllegalArgumentException("Length must be in 1 to 10");
        }
        if(maxAttempts <= 0){
            throw new IllegalArgumentException("attempt number must be > 0");
        }
        if(timeLimitSeconds <= 0 && isTimeLimitEnabled){
            throw new IllegalArgumentException("time is incorrect, must be > 0");
        }
    }

}
