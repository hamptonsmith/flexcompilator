package com.shieldsbetter.flexcompilator.fiddle;

import com.shieldsbetter.flexcompilator.NoMatchException;
import com.shieldsbetter.flexcompilator.ParseHead;
import com.shieldsbetter.flexcompilator.WellFormednessException;
import com.shieldsbetter.flexcompilator.matchers.CAny;
import com.shieldsbetter.flexcompilator.matchers.COneOf;
import com.shieldsbetter.flexcompilator.matchers.CSubtract;
import com.shieldsbetter.flexcompilator.matchers.MAlternatives;
import com.shieldsbetter.flexcompilator.matchers.MCapture;
import com.shieldsbetter.flexcompilator.matchers.MEndOfInput;
import com.shieldsbetter.flexcompilator.matchers.MError;
import com.shieldsbetter.flexcompilator.matchers.MLiteral;
import com.shieldsbetter.flexcompilator.matchers.MSequence;
import com.shieldsbetter.flexcompilator.matchers.MRepeated;
import com.shieldsbetter.flexcompilator.matchers.Matcher;

public class Sbsdl {
    private static final Matcher STRING_LITERAL =
            new MSequence(
                    new MLiteral("'"),
                    new MCapture(new MRepeated(new MAlternatives(
                            new MError(new MLiteral("\n"), "Encountered end of "
                                    + "line before close of string literal."),
                            new CSubtract(
                                    CAny.INSTANCE, new COneOf('\'', '\\')),
                            new MSequence(new MLiteral("\\"),
                                    new MAlternatives(
                                            new MLiteral("n"),
                                            new MLiteral("r"),
                                            new MLiteral("'"),
                                            new MError(CAny.INSTANCE,
                                                    "Unrecognized control "
                                                            + "character in "
                                                            + "string."))),
                            new MError(MEndOfInput.INSTANCE, "Encountered end "
                                    + "of input before string closed.")
                    ))),
                    new MLiteral("'"));
    
    public static void main(String[] args) throws NoMatchException, WellFormednessException {
        ParseHead h = new ParseHead("'testing'");
        h.advanceOver(STRING_LITERAL);
        
        System.out.println(h.nextCapture());
    }
}
