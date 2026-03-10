package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class GameSettingsTest {
    @Test
    void createValidSettings(){
        GameSettings settings = new GameSettings(4, 10, 60, true);

        assertEquals(4, settings.sequenceLength());
        assertEquals(10, settings.maxAttempts());
        assertEquals(60, settings.timeLimitSeconds());
        assertTrue(settings.isTimeLimitEnabled());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 11, -1})
    void invalidLength(int invalidLength){
        assertThrows(IllegalArgumentException.class, () -> new GameSettings(invalidLength, 10, 0, false));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -5})
    void invalidAttempts(int invalidAttempts) {
        assertThrows(IllegalArgumentException.class,
                () -> new GameSettings(4, invalidAttempts, 0, false));
    }
}
