package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MExclude implements Matcher {
    private final Matcher myExclusion;
    private final Matcher myFrom;
    
    public MExclude(Matcher exclude, Matcher from) {
        myExclusion = exclude;
        myFrom = from;
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        h.skip();
        
        int charCt = -1;
        
        h.pushState();
        
        boolean matchedExclusion;
        try {
            h.require(myExclusion);
            matchedExclusion = true;
            h.tossState();
        }
        catch (NoMatchException nme) {
            h.tossState();
            charCt = h.require(myFrom);
            matchedExclusion = false;
        }
        
        if (matchedExclusion) {
            throw NoMatchException.INSTANCE;
        }
        
        return charCt;
    }
}
