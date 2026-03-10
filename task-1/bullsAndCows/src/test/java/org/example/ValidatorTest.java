package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {
    private final InputValidator validator = new InputValidator();
    private final int LENGTH = 4;

    @Test
    void testEmpty(){
        assertThrows(IllegalArgumentException.class, () -> validator.validate(null, LENGTH));
        assertThrows(IllegalArgumentException.class, () -> validator.validate("", LENGTH));
    }

    @Test
    void invalidLength(){
        assertThrows(IllegalArgumentException.class, () -> validator.validate("123", LENGTH));
        assertThrows(IllegalArgumentException.class, () -> validator.validate("12345", LENGTH));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1a34", "1.34", "1 34"})
    void digitOnly(String invalidInput){
        assertThrows(IllegalArgumentException.class, () -> validator.validate(invalidInput, LENGTH));
    }

    @Test
    void testUniq(){
        assertThrows(IllegalArgumentException.class, () -> validator.validate("1123", LENGTH));
    }

    @Test
    void valid(){
        assertDoesNotThrow(() -> validator.validate("1234", LENGTH));
    }
}
