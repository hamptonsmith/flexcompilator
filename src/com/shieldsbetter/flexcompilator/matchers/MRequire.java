package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MRequire implements Matcher {
    private final Matcher myBase;
    private final String myMessage;
    
    public MRequire(Matcher baseMatcher, String message) {
        myBase = baseMatcher;
        myMessage = message;
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        int result;
        
        try {
            result = h.advanceOver(myBase);
        }
        catch (NoMatchException nme) {
            throw new WellFormednessException(myMessage);
        }
        
        return result;
    }
}
