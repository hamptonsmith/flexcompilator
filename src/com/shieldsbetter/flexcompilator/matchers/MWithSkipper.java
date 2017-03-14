package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MWithSkipper implements Matcher{
    private final Matcher myBase;
    private final Matcher mySkipper;
    
    public MWithSkipper(Matcher base, Matcher skipper) {
        myBase = base;
        mySkipper = skipper;
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        Matcher oldSkipper = h.getSkip();
        
        h.setSkip(mySkipper);
        h.skip(oldSkipper);
        
        int charCt;
        try {
            charCt = h.require(myBase);
        }
        finally {
            h.setSkip(oldSkipper);
        }
        
        return charCt;
    }
}
