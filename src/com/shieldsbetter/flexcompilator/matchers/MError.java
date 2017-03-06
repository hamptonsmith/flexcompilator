package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MError implements Matcher {
    private final Matcher myMatcher;
    private final String myMessage;
    
    public MError(Matcher matcher, String msg) {
        myMatcher = matcher;
        myMessage = msg;
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        myMatcher.match(h);
        throw new WellFormednessException(myMessage);
    }
}
