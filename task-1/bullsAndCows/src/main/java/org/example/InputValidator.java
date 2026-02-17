package org.example;

import java.util.HashSet;
import java.util.Set;

public class InputValidator {
    public void validate(String input, int requiredLength){
        if(input == null || input.isEmpty()){
            throw new IllegalArgumentException("Input cannot be empty.");
        }

        if(input.length() != requiredLength){
            throw new IllegalArgumentException("The length of number must be only " + requiredLength);
        }

        if(!input.matches("\\d+")){
            throw new IllegalArgumentException("Number must contain only digits.");
        }

        if(!hasUniqueCharacters(input)){
            throw new IllegalArgumentException("Digits must be unique.");
        }
    }

    private boolean hasUniqueCharacters(String input){
        Set<Character> uniqueChars = new HashSet<>();
        for(char c : input.toCharArray()){
            if(!uniqueChars.add(c)){
                return false;
            }
        }
        return true;
    }
}
