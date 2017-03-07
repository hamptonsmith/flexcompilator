package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MAlternatives implements Matcher {
    private final List<Matcher> myAlternatives;
    
    public MAlternatives(Matcher ... alternatives) {
        myAlternatives = Arrays.asList(alternatives);
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        int length = -1;
        Iterator<Matcher> alternativeIter = myAlternatives.iterator();
        while (length == -1 && alternativeIter.hasNext()) {
            Matcher alternative = alternativeIter.next();
            
            h.pushOffset();
            try {
                length = alternative.match(h);
                h.tossOffset();
            }
            catch (NoMatchException nme) {
                // No problem.
                h.popOffset();
            }
        }
        
        if (length == -1) {
            throw NoMatchException.INSTANCE;
        }
        
        return length;
    }
}
