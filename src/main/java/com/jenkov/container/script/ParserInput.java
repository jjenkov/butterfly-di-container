package com.jenkov.container.script;

import com.jenkov.container.script.ParserException;

import java.io.*;
import java.util.Stack;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ParserInput {

    protected ScriptTokenizer scriptTokenizer = null;
    protected Stack<ParserMark> marks  = new Stack<ParserMark>();

    /**
     * @deprecated Use the constructor that takes a Reader instead.
     * @param input
     */
    public ParserInput(InputStream input) {
        this.scriptTokenizer = new ScriptTokenizer(new ScriptTokenizerInputBuffer(new InputStreamReader(input)));
    }

    public ParserInput(Reader reader) {
        this.scriptTokenizer = new ScriptTokenizer(new ScriptTokenizerInputBuffer(reader));
    }
    

    public ParserInput(String input){
        this.scriptTokenizer = new ScriptTokenizer(new ScriptTokenizerInputBuffer(new StringReader(input)));
    }






    public void factoryStart(){
        this.scriptTokenizer.factoryStart();
    }

    public boolean isNextElseBacktrack(Token expectedToken){
        mark();
        Token nextToken = nextToken();
        boolean matches = expectedToken.equals(nextToken);
        if(matches) clearMark();
        else        backtrack();
        return matches;
    }

    public void assertNextToken(Token token){
        Token nextToken = nextToken();
        if(nextToken == null || !nextToken.equals(token)){
            throw new ParserException(
                    "ParserInput", "ASSERT_NEXT_TOKEN",
                    "Error (" + this.scriptTokenizer.getLineNo() + ", " + this.scriptTokenizer.getCharNo() + 
                            "): Expected token " + token + " but found " + nextToken);
        }
    }

    public Token lookAhead() {
        mark();
        Token nextToken = nextToken();
        backtrack();
        return nextToken;
    }

    public Token markAndNextToken(){
        mark();
        return nextToken();
    }

    public Token nextToken()  {
        Token nextToken = this.scriptTokenizer.nextToken();
        if(nextToken == null) return null;
        return nextToken;
    }

    public void assertNoMarks(){
        if(this.marks.size() > 0){
            throw new ParserException(
                    "ParserInput", "ASSERT_NO_MARKS",
                    "There should have been no marks at the current parsing point");
        }
    }

    public int mark(){
        ParserMark mark = this.scriptTokenizer.mark();
        this.marks.push(mark);
        return this.marks.size();
    }

    public int backtrack(){
        ParserMark mark = this.marks.pop();
        this.scriptTokenizer.backtrackTo(mark);
        return this.marks.size() + 1;
    }

    public void clearMark(){
        this.marks.pop();
    }

    public boolean hasMark(){
        return this.marks.size() > 0;
    }

}