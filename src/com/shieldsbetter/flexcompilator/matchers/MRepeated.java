package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MRepeated implements Matcher {
    private final Matcher myBase;
    private final Integer myMinimum;
    private final Integer myMaximum;
    
    public MRepeated(Matcher ... ms) {
        this(new MSequence(ms), null, null);
    }
    
    public MRepeated(Matcher m) {
        this(m, null, null);
    }
    
    public MRepeated(Matcher m, Integer minimum, Integer maximum) {
        myBase = m;
        myMinimum = minimum;
        myMaximum = maximum;
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        int result = 0;
        
        int instances = 0;
        int justNow;
        do {
            try {
                justNow = myBase.match(h);
                result += justNow;
                instances++;
            }
            catch (NoMatchException nme) {
                justNow = 0;
            }
        } while (justNow > 0);
        
        if (myMinimum != null && instances < myMinimum) {
            throw NoMatchException.INSTANCE;
        }
        
        if (myMaximum != null && instances > myMaximum) {
            throw NoMatchException.INSTANCE;
        }
        
        return result;
    }
}
