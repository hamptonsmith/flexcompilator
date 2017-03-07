package com.shieldsbetter.flexcompilator.matchers;

import java.util.HashSet;
import java.util.Set;

public class COneOf extends AbstractCharacterMatcher {
    private final Set<Integer> myChars = new HashSet<>();
    
    public COneOf(int ... chars) {
        super("One of " + toChars(chars));
        
        for (int c : chars) {
            myChars.add(c);
        }
    }
    
    private static String toChars(int[] chars) {
        return new String(chars, 0, chars.length);
    }
    
    @Override
    public boolean matchCharacter(int i) {
        return myChars.contains(i);
    }
}
