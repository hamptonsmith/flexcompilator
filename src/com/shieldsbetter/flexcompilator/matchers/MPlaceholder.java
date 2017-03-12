package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MPlaceholder implements Matcher {
    private Matcher myBase;
    
    public void fillIn(Matcher ... ms) {
        fillIn(new MSequence(ms));
    }
    
    public void fillIn(Matcher m) {
        if (myBase != null) {
            throw new IllegalStateException("Already filled in.");
        }
        
        myBase = m;
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        if (myBase == null) {
            throw new IllegalStateException("Placeholder not filled in.");
        }
        
        return h.advanceOver(myBase);
    }
}
