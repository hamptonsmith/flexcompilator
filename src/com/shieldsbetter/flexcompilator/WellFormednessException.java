package com.shieldsbetter.flexcompilator;

public class WellFormednessException extends Exception {
    private final String myLineContents;
    private final int myLineNumber;
    private final int myColNumber;
    private final String myAlignmentPrefix;
    
    public WellFormednessException(String msg, ParseHead state) {
        this(msg, state.getPosition());
    }
    
    public WellFormednessException(String msg, ParseHead.Position state) {
        super(msg);
        
        myLineContents = state.getLineContents();
        myLineNumber = state.getLineNumber();
        myColNumber = state.getColumn();
        myAlignmentPrefix = state.getAlignmentPrefix();
    }
    
    @Override
    public String toString() {
        String result = "Line " + myLineNumber + ": " + getMessage() + "\n\n";
        
        result += myLineContents + "\n";
        result += myAlignmentPrefix + "^\n";
        
        return result;
    }
    
    public String getLineContents() {
        return myLineContents;
    }
    
    public int getLineNumber() {
        return myLineNumber;
    }
    
    public int getColNumber() {
        return myColNumber;
    }
    
    public String getAlignmentPrefix() {
        return myAlignmentPrefix;
    }
}
