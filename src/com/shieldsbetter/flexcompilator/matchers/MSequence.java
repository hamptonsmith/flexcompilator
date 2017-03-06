package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;
import java.util.Arrays;
import java.util.List;

public class MSequence implements Matcher {
    private final List<Matcher> mySubmatchers;
    
    public MSequence(Matcher ... ms) {
        mySubmatchers = Arrays.asList(ms);
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        int soFar = 0;
        for (Matcher m : mySubmatchers) {
            soFar += m.match(h);
        }
        
        return soFar;
    }
}
