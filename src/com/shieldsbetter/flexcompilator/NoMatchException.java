package com.shieldsbetter.flexcompilator;

public class NoMatchException extends Exception {
    public static final NoMatchException INSTANCE = new NoMatchException();
    
    private NoMatchException() { }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
