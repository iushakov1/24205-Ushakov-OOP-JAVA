package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomSecretGenerator implements SecretGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RandomSecretGenerator.class);
    @Override
    public String generate(int length){
        if(length < 1 || length > 10){
            logger.error("incorrect length in generate: length={}", length);
            throw new IllegalArgumentException("Length must be between 1 and 10");
        }
        List<Integer> digits = new ArrayList<>();
        for(int i = 0; i < 10; ++i){
            digits.add(i);
        }

        Collections.shuffle(digits);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; ++i){
            sb.append(digits.get(i));
        }
        String result = sb.toString();
        logger.debug("Generated secret sequence: {}", result);
        return result;
    }
}
