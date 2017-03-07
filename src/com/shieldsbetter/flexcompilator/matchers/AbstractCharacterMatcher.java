package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public abstract class AbstractCharacterMatcher implements Matcher {
    private final String myName;
    
    public AbstractCharacterMatcher(String s) {
        myName = s;
    }
    
    public String getName() {
        return myName;
    }
    
    public abstract boolean matchCharacter(int i);
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        
        System.out.println(myName);
        boolean charMatch = matchCharacter(h.peekChar());
        
        if (charMatch) {
            h.skip(1);
        }
        else {
            throw NoMatchException.INSTANCE;
        }
        
        return 1;
    }
}
