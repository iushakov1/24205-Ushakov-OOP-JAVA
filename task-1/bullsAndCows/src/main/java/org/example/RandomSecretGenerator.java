package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomSecretGenerator implements SecretGenerator {
    @Override
    public String generate(int length){
        if(length < 1 || length > 10){
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
        return sb.toString();
    }
}
