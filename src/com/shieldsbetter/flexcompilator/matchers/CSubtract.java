package com.shieldsbetter.flexcompilator.matchers;

public class CSubtract extends AbstractCharacterMatcher {
    private final AbstractCharacterMatcher myIn;
    private final AbstractCharacterMatcher myButNotIn;
    
    public CSubtract(
            AbstractCharacterMatcher in, AbstractCharacterMatcher butNotIn) {
        super("Subtract " + in.getName() + " - " + butNotIn.getName());
        myIn = in;
        myButNotIn = butNotIn;
    }

    @Override
    public boolean matchCharacter(int i) {
        return myIn.matchCharacter(i) && !myButNotIn.matchCharacter(i);
    }
}
