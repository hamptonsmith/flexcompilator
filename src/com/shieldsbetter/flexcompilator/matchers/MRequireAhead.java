package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MRequireAhead implements Matcher {
    private final Matcher myMatcher;
    private final String myNotFoundMessage;
    
    public MRequireAhead(Matcher m, String notFoundMesssage) {
        myMatcher = m;
        myNotFoundMessage = notFoundMesssage;
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        h.skip();
        
        h.pushState();
        try {
            h.require(myMatcher);
        }
        catch (NoMatchException nme) {
            h.tossState();
            throw new WellFormednessException(myNotFoundMessage, h);
        }
        
        h.tossState();
        
        return 0;
    }
}
