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
        // Make sure we're queued up to the thing we're potentially about to
        // forbid so that we can throw a sensible exception.
        h.skip();
        
        h.pushState();
        
        try {
            h.require(myMatcher);
            throw new WellFormednessException(myMessage, h);
        }
        catch (NoMatchException nme) {
            // Fall through.
        }
        finally {
            // If we just matched, we want to get back to the state just before
            // the match so we can throw a sensible exception.  If we didn't
            // just match, let's get back into the state we entered in so the
            // parser can carry on.  Either way we do the same thing.
            h.tossState();
        }
        
        return 0;
    }
}
