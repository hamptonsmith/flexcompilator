package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MCapture implements Matcher {
    private final Matcher myBase;
    
    public MCapture(Matcher m) {
        myBase = m;
    }

    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        h.pushOffset();
        
        int length;
        try {
            length = myBase.match(h);
            h.captureString(length);
            
            h.tossOffset();
        }
        catch (NoMatchException nme) {
            h.popOffset();
            throw nme;
        }
        
        return length;
    }
}
