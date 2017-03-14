package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.Utils;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public class MLiteral implements Matcher {
    private final int[] myLiteral;
    
    public MLiteral(String literal) {
        myLiteral = Utils.codepoints(literal);
    }
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        h.require(myLiteral);
        return myLiteral.length;
    }
}
