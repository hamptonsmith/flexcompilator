package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MOptional implements Matcher {
    private final Matcher myBase;
    
    public MOptional(Matcher ... ms) {
        myBase = new MSequence(ms);
    }
    
    public MOptional(Matcher m) {
        myBase = m;
    }
    
    public void onOmitted(ParseHead h) { }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        int characters;
        
        try {
            characters = h.require(myBase);
        }
        catch (NoMatchException nme) {
            // No problem.
            onOmitted(h);
            characters = 0;
        }
        
        return characters;
    }
}
