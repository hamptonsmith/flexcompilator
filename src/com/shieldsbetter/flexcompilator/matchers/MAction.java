package com.shieldsbetter.flexcompilator.matchers;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.ParseHead.Position;
import com.shieldsbetter.flexcompilator.WellFormednessException;

public abstract class MAction implements Matcher {
    private final boolean myNotePositionFlag;
    private final Matcher myBase;
    
    private ParseHead.Position myNotedPosition;
    
    public MAction(Matcher base) {
        this(false, base);
    }
    
    public MAction(boolean notePosition, Matcher base) {
        myNotePositionFlag = notePosition;
        myBase = base;
    }
    
    public Position getNotedPosition() {
        if (myNotedPosition == null) {
            throw new IllegalStateException();
        }
        
        return myNotedPosition;
    }
    
    public void before(ParseHead h) {}
    public void onMatched(ParseHead h) throws WellFormednessException {}
    public void onFailed(ParseHead h) {}
    
    @Override
    public int match(ParseHead h)
            throws NoMatchException, WellFormednessException {
        before(h);
        
        if (myNotePositionFlag) {
            myNotedPosition = h.getPosition();
        }
        
        int result;
        try {
            result = h.require(myBase);
            onMatched(h);
        }
        catch (NoMatchException nme) {
            onFailed(h);
            throw nme;
        }
        
        return result;
    }
}
