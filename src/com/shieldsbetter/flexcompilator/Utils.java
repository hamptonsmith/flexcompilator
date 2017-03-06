package com.shieldsbetter.flexcompilator;

public class Utils {
    public static int[] codepoints(String input) {
        int inputLength = input.length();
        int[] protoCodepoints = new int[inputLength];
        
        int javaCharCt = 0;
        int codepointCt = 0;
        while (javaCharCt < inputLength) {
            int codepoint = input.codePointAt(javaCharCt);
            protoCodepoints[codepointCt] = codepoint;
            javaCharCt += Character.charCount(codepoint);
            codepointCt++;
        }
        
        int[] result = new int[codepointCt];
        System.arraycopy(protoCodepoints, 0, result, 0, codepointCt);
        
        return result;
    }
}
