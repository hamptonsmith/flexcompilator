package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MForbid implements Matcher {
    private final Matcher myMatcher;
    private final String myMessage;
    
    public MForbid(Matcher matcher, String msg) {
        myMatcher = matcher;
        myMessage = msg;
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        h.advanceOver(myMatcher);
        throw new WellFormednessException(myMessage);
    }
}
