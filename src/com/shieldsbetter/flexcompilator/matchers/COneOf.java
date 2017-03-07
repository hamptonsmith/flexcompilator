package com.shieldsbetter.flexcompilator.matchers;

import java.util.HashSet;
import java.util.Set;

public class COneOf extends AbstractCharacterMatcher {
    private final Set<Integer> myChars = new HashSet<>();
    
    public COneOf(int ... chars) {
        for (int c : chars) {
            myChars.add(c);
        }
    }
    
    @Override
    public boolean matchCharacter(int i) {
        return myChars.contains(i);
    }
}
