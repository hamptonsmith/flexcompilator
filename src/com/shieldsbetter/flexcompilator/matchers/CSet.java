package com.shieldsbetter.flexcompilator.matchers;

public class CSet {
    public static final Matcher ISO_LATIN_DIGIT =
            new COneOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    
    public static final Matcher LETTER =
            new AbstractCharacterMatcher() {
                @Override
                public boolean matchCharacter(int i) {
                    return Character.isLetter(i);
                }
            };
    
    public static final Matcher LETTER_OR_DIGIT =
            new AbstractCharacterMatcher() {
                @Override
                public boolean matchCharacter(int i) {
                    return Character.isLetterOrDigit(i);
                }
            };
}
