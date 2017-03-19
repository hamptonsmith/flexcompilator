package com.shieldsbetter.flexcompilator;

import com.shieldsbetter.flexcompilator.matchers.AbstractCharacterMatcher;
import com.shieldsbetter.flexcompilator.matchers.Matcher;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ParseHead {
    private final int[] myCodepoints;
    private final ArrayDeque<State> myStates = new ArrayDeque<>();
    
    public ParseHead(String input) {
        this(input, 1, 0);
    }
    
    public ParseHead(String input, int lineStart, int colStart) {
        myStates.push(new State(lineStart, colStart));
        
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
    
    public void pushOnParseStack(Object o) {
        if (o.equals("from")) {
            throw new RuntimeException();
        }
        
        myStates.peek().pushOnParseStack(o);
    }
    
    public Object popFromParseStack() {
        Object result = null;
        
        boolean found = false;
        Iterator<State> stateIter = myStates.iterator();
        while (!found && stateIter.hasNext()) {
            State state = stateIter.next();
            
            if (!state.isParseStackEmpty()) {
                result = state.popFromParseStack();
                found = true;
            }
        }
        
        return result;
    }
    
    public Object peekFromParseStack() {
        Object result = null;
        
        boolean found = false;
        Iterator<State> stateIter = myStates.iterator();
        while (!found && stateIter.hasNext()) {
            State state = stateIter.next();
            
            if (!state.isParseStackEmpty()) {
                result = state.peekFromParseStack();
                found = true;
            }
        }
        
        return result;
    }
    
    public boolean isParseStackEmpty() {
        boolean result = true;
        
        Iterator<State> stateIter = myStates.iterator();
        while (result && stateIter.hasNext()) {
            result = stateIter.next().isParseStackEmpty();
        }
        
        return result;
    }
    
    public Deque<Object> getParseStackCopy() {
        LinkedList<Object> result = new LinkedList<>();
        
        for (State s : myStates) {
            result.addAll(s.myParseStack);
        }
        
        return result;
    }
    
    public void skip() throws WellFormednessException {
        doSkip();
    }
    
    public void pushState() {
        myStates.push(new State(myStates.peek()));
    }
    
    public void tossState() {
        myStates.pop();
    }
    
    public void acceptState() {
        State toAccept = myStates.pop();
        myStates.peek().absorb(toAccept);
    }
    
    public String getLine() {
        return myStates.peek().getLine();
    }
    
    public int getLineNumber() {
        return myStates.peek().getLineNumber();
    }
    
    public int getColumn() {
        return myStates.peek().getColumn();
    }
    
    public String getAlignmentPrefix() {
        return myStates.peek().getAlignmentPrefix();
    }
    
    public String remainingText() {
        int offset = myStates.peek().getOffset();
        return new String(myCodepoints, offset, myCodepoints.length - offset);
    }
    
    public void setSkip(Matcher skipper) {
        myStates.peek().setSkipper(skipper);
    }
    
    public Matcher getSkip() {
        return myStates.peek().getSkipper();
    }
    
    public boolean hasNextChar() throws WellFormednessException {
        doSkip();
        
        return myStates.peek().getOffset() < myCodepoints.length;
    }
    
    public void captureString(int length) {
        myStates.peek().captureString(length);
    }
    
    public String popCapture() {
        String result;
        try {
            result = myStates.peek().popCapture();
        }
        catch (NoSuchElementException nsee) {
            if (myStates.size() > 1) {
                throw new IllegalStateException(
                        "Capture pop past most recent backtrack point.");
            }
            else {
                throw new IllegalStateException("No available captures.");
            }
        }
        
        return result;
    }
    
    public int skip(Matcher m) throws WellFormednessException {
        int characterCt;
        
        try {
            if (m != null) {
                characterCt = requireNoSkip(m);
            }
            else {
                characterCt = 0;
            }
        }
        catch (NoMatchException nme) {
            // No problem.
            characterCt = 0;
        }
        
        return characterCt;
    }
    
    public void require(int[] literal)
            throws NoMatchException, WellFormednessException {
        doSkip();
        
        pushState();
        
        try {
            myStates.peek().advanceOver(literal);
            
            acceptState();
        }
        catch (NoMatchException nme) {
            tossState();
            throw nme;
        }
    }
    
    public int require(Matcher m)
            throws NoMatchException, WellFormednessException {
        return doSkip() + requireNoSkip(m);
    }
    
    private int requireNoSkip(Matcher m)
            throws NoMatchException, WellFormednessException {
        int characterCt;
        pushState();
        
        try {
            characterCt = m.match(this);
            acceptState();
        }
        catch (NoMatchException nme) {
            tossState();
            throw NoMatchException.INSTANCE;
        }
        
        return characterCt;
    }
    
    private int doSkip() throws WellFormednessException {
        int characterCt;
        if (myStates.peek().getSkipper() != null) {
            characterCt = skip(myStates.peek().getSkipper());
        }
        else {
            characterCt = 0;
        }
        return characterCt;
    }
    
    public void requireChar(AbstractCharacterMatcher m)
            throws NoMatchException, WellFormednessException {
        myStates.peek().requireChar(m);
    }
    
    private class State {
        private int myOffset;
        private final Deque<String> myCaptures = new LinkedList<>();
        private final Deque<Object> myParseStack = new LinkedList<>();
        private Matcher mySkipper;
        private int myLineNumber;
        private int myCol;
        private int myLineStart;
        private final StringBuilder myAlignmentPrefix = new StringBuilder();
        
        public State(int lineNumber, int col) {
            myLineNumber = lineNumber;
            myCol = col;
        }
        
        public State(State original) {
            myOffset = original.myOffset;
            mySkipper = original.mySkipper;
            myLineNumber = original.myLineNumber;
            myCol = original.myCol;
            myLineStart = original.myLineStart;
            myAlignmentPrefix.append(original.myAlignmentPrefix);
        }
        
        public boolean isParseStackEmpty() {
            return myParseStack.isEmpty();
        }
        
        public void pushOnParseStack(Object o) {
            myParseStack.push(o);
        }
        
        public Object popFromParseStack() {
            return myParseStack.pop();
        }
        
        public Object peekFromParseStack() {
            return myParseStack.peek();
        }
        
        public void captureString(int length) {
            myCaptures.push(
                    new String(myCodepoints, myOffset - length, length));
        }
        
        public String popCapture() {
            return myCaptures.pop();
        }
        
        public int getOffset() {
            return myOffset;
        }
        
        public int getLineNumber() {
            return myLineNumber;
        }
        
        public int getColumn() {
            return myCol;
        }
        
        public String getAlignmentPrefix() {
            return myAlignmentPrefix.toString();
        }
        
        public String getLine() {
            int offset = myLineStart;
            while (offset < myCodepoints.length
                    && myCodepoints[offset] != '\n') {
                offset++;
            }

            return new String(myCodepoints, myLineStart, offset - myLineStart);
        }
        
        public Matcher getSkipper() {
            return mySkipper;
        }
        
        public void setSkipper(Matcher m) {
            mySkipper = m;
        }
        
        public void absorb(State other) {
            myOffset = other.myOffset;
            mySkipper = other.mySkipper;
            myLineNumber = other.myLineNumber;
            myCol = other.myCol;
            myLineStart = other.myLineStart;
            
            myAlignmentPrefix.delete(0, myAlignmentPrefix.length());
            myAlignmentPrefix.append(other.myAlignmentPrefix);
            
            while (!other.myCaptures.isEmpty()) {
                myCaptures.push(other.myCaptures.removeLast());
            }
            
            while (!other.myParseStack.isEmpty()) {
                myParseStack.push(other.myParseStack.removeLast());
            }
        }
        
        public int peekChar() throws NoMatchException, WellFormednessException {
            doSkip();

            if (myOffset == myCodepoints.length) {
                throw NoMatchException.INSTANCE;
            }

            return myCodepoints[myOffset];
        }
        
        public void require(int character) throws NoMatchException {
            if (myOffset == myCodepoints.length) {
                throw NoMatchException.INSTANCE;
            }

            if (myCodepoints[myOffset] != character) {
                throw NoMatchException.INSTANCE;
            }

            advanceChar();
        }
        
        public void advanceOver(int[] characters) throws NoMatchException {
            for (int c : characters) {
                require(c);
            }
        } 
        
        public void requireChar(AbstractCharacterMatcher m)
                throws NoMatchException, WellFormednessException {
            boolean matches = m.matchCharacter(peekChar());
            if (matches) {
                skip(1);
            }
            else {
                throw NoMatchException.INSTANCE;
            }
        }
        
        public void skip(int count) {
            for (int i = 0; i < count; i++) {
                advanceChar();
            }
        }
        
        private void advanceChar() {
            int c = myCodepoints[myOffset];
            if (c == '\n') {
                myLineNumber++;
                myCol = 0;
                myLineStart = myOffset + 1;
                myAlignmentPrefix.delete(0, myAlignmentPrefix.length());
            }
            else {
                if (c == '\t') {
                    myAlignmentPrefix.append("\t");
                }
                else {
                    myAlignmentPrefix.append(" ");
                }

                myCol++;
            }

            myOffset++;
        }
    }
}
