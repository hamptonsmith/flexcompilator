package com.shieldsbetter.flexcompilator;

import com.shieldsbetter.flexcompilator.matchers.Matcher;
import java.util.ArrayDeque;
import java.util.Deque;

public class ParseHead {
    private final int[] myCodepoints;
    
    private final Deque<Integer> mySavedOffset = new ArrayDeque<>();
    
    private int myOffset;
    
    private final Deque<String> myCaptures = new ArrayDeque<>();
    
    private Matcher mySkipper = null;
    
    public ParseHead(String input) {
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
        
        myCodepoints = new int[codepointCt];
        System.arraycopy(protoCodepoints, 0, myCodepoints, 0, codepointCt);
    }
    
    public String remainingText() {
        return new String(
                myCodepoints, myOffset, myCodepoints.length - myOffset);
    }
    
    public void setSkip(Matcher skipper) {
        mySkipper = skipper;
    }
    
    public Matcher getSkip() {
        return mySkipper;
    }
    
    public boolean hasNextChar() throws WellFormednessException {
        doSkip();
        
        return myOffset < myCodepoints.length;
    }
    
    public void pushOffset() {
        mySavedOffset.push(myOffset);
    }
    
    public void popOffset() {
        myOffset = mySavedOffset.pop();
    }
    
    public void tossOffset() {
        mySavedOffset.pop();
    }
    
    public void captureString(int length) {
        myCaptures.add(new String(myCodepoints, mySavedOffset.peek(), length));
    }
    
    public String nextCapture() {
        return myCaptures.remove();
    }
    
    public int peekChar() throws NoMatchException, WellFormednessException {
        doSkip();
        
        if (myOffset == myCodepoints.length) {
            throw NoMatchException.INSTANCE;
        }
        
        return myCodepoints[myOffset];
    }
    
    public void skip(int count) {
        myOffset += count;
    }
    
    public void skip(Matcher m) throws WellFormednessException {
        try {
            if (m != null) {
                advanceOverNoSkip(m);
            }
        }
        catch (NoMatchException nme) {
            // No problem.
        }
    }
    
    public void advanceOver(int[] literal)
            throws NoMatchException, WellFormednessException {
        doSkip();
        
        mySavedOffset.push(myOffset);
        
        try {
            for (int c : literal) {
                advanceOver(c);
            }
            mySavedOffset.pop();
        }
        catch (NoMatchException nme) {
            myOffset = mySavedOffset.pop();
            throw nme;
        }
    }
    
    public void advanceOver(Matcher m)
            throws NoMatchException, WellFormednessException {
        doSkip();
        advanceOverNoSkip(m);
    }
    
    private void advanceOverNoSkip(Matcher m)
            throws NoMatchException, WellFormednessException {
        mySavedOffset.push(myOffset);
        
        try {
            m.match(this);
            mySavedOffset.pop();  // Throw it away.
        }
        catch (NoMatchException nme) {
            myOffset = mySavedOffset.pop();  // Restore.
            throw NoMatchException.INSTANCE;
        }
    }
    
    private void doSkip() throws WellFormednessException {
        if (mySkipper != null) {
            skip(mySkipper);
        }
    }
    
    private void advanceOver(int character) throws NoMatchException {
        if (myOffset == myCodepoints.length) {
            throw NoMatchException.INSTANCE;
        }
        
        if (myCodepoints[myOffset] != character) {
            throw NoMatchException.INSTANCE;
        }
        
        myOffset++;
    }
}
