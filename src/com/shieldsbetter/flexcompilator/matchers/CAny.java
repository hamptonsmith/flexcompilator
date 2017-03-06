package com.shieldsbetter.flexcompilator.matchers;

public class CAny extends AbstractCharacterMatcher {
    public static final CAny INSTANCE = new CAny();
    
    private CAny() { }
    
    @Override
    public boolean matchCharacter(int i) {
        return true;
    }
}
