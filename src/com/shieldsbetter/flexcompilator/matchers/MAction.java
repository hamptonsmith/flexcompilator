package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public abstract class MAction implements Matcher {
    private final Matcher myBase;
    
    public MAction(Matcher base) {
        myBase = base;
    }
    
    public void before(ParseHead h) {}
    public void onMatched(ParseHead h) throws WellFormednessException {}
    public void onFailed(ParseHead h) {}
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        before(h);
        
        int result;
        try {
            result = h.advanceOver(myBase);
            onMatched(h);
        }
        catch (NoMatchException nme) {
            onFailed(h);
            throw nme;
        }
        
        return result;
    }
}
