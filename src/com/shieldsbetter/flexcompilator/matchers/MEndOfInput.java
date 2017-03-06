package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MEndOfInput implements Matcher {
    public static final MEndOfInput INSTANCE = new MEndOfInput();
    
    private MEndOfInput() { }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        if (h.hasNextChar()) {
            throw NoMatchException.INSTANCE;
        }
        
        return 0;
    }
}
