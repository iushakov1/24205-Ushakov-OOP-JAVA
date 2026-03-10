package org.example;

public interface GameUI {
    void showMessage(String message);

    int getUserIntInput(String message);
    String getUserInput();

    void showGuessResult(GuessResult result);

    void showGameStatus(int attemptsLeft, long timeLeftSeconds);
}
