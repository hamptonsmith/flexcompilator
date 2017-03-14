package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public abstract class MDo implements Matcher {
    public abstract void run();
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        return 0;
    }
}
