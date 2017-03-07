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
        int length;
        try {
            length = h.advanceOver(myBase);
            h.captureString(length);
        }
        catch (NoMatchException nme) {
            throw nme;
        }
        
        return length;
    }
}
