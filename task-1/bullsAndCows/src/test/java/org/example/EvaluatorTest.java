package org.example;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EvaluatorTest {
    private final BullsAndCowsEvaluator evaluator = new BullsAndCowsEvaluator();

    @Test
    public void testAllBulls(){
        String secret = "1234";
        String guess = "1234";

        GuessResult result = evaluator.evaluate(secret, guess);

        assertEquals(4, result.bulls(), "must be 4 bulls");
        assertEquals(0, result.cows(), "must be 0 cows");
        assertTrue(result.isWin(), "it must be win");
    }

    @Test
    public void testOnlyCows(){
        GuessResult result = evaluator.evaluate("1234", "4321");

        assertEquals(0, result.bulls());
        assertEquals(4, result.cows());
    }

    @Test
    public void testMixedResults(){
        GuessResult result = evaluator.evaluate("1234", "1356");

        assertEquals(1, result.bulls());
        assertEquals(1, result.bulls());
    }
}
