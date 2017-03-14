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
        
        // Let's get queued up right to the thing we're about to require so that
        // we can have a sensible exception.
        h.skip();
        
        try {
            result = h.require(myBase);
        }
        catch (NoMatchException nme) {
            throw new WellFormednessException(myMessage, h);
        }
        
        return result;
    }
}
