package com.jenkov.container.script;

import java.io.Reader;
import java.io.IOException;

/**

 */
public class ScriptTokenizerInputBuffer {

    protected int    maxFactorySize = 128 * 128;  // 128 lines with 128 characters each = 16K
    protected int    index          = 0;
    protected int    endIndex       = 0;  //used if buffer is larger than input read
    protected Reader reader         = null;
    public    char[] buffer         = null;

    protected int       factoryStartIndex  = 0;
    protected boolean   endOfStreamReached = false;
    protected TokenPool tokenPool          = new TokenPool();


    public ScriptTokenizerInputBuffer(Reader reader) {
        init(reader, this.maxFactorySize);
    }

    public ScriptTokenizerInputBuffer(Reader reader, int maxFactorySize) {
        init(reader, maxFactorySize);
    }

    public void init(Reader reader, int maxFactorySize){
        this.maxFactorySize = maxFactorySize;
        this.reader = reader;
        this.buffer = new char[maxFactorySize];  //max factory size as buffer size.
        try {
            this.endIndex = this.reader.read(this.buffer);
        } catch (IOException e) {
            throw new ParserException(
                    "ScriptTokenizerInputBuffer", "ERROR_READING_FROM_READER",

                    "Error reading data from Reader into ScriptTokenizerInputBuffer", e);
        }
    }

    public void factoryStart() {
        this.factoryStartIndex = this.index;
        this.tokenPool.freeAll();
    }

    public int read(){
        if(index < endIndex  ) return buffer[index++];
        if(endOfStreamReached) return -1;

        compressBuffer();
        fillBuffer();

        return read();
        
    }

    /* temp method... remove later*/
    public void unread(char achar){
        index--;
    }

    public void unread(){
        index--;
    }


    private void compressBuffer() {
        for(int i=this.factoryStartIndex, j=0;  i< this.endIndex;) {
            this.buffer[j++] = this.buffer[i++];
        }
        this.endIndex -= this.factoryStartIndex;
        this.index    -= this.factoryStartIndex;
        this.tokenPool.correctIndexOfTakenTokens(this.factoryStartIndex);
        this.factoryStartIndex = 0;
    }

    private void fillBuffer(){
        try {
            int charsRead = this.reader.read(this.buffer, this.endIndex, this.buffer.length - this.endIndex);
            if (charsRead == -1) {
                this.endOfStreamReached = true;
            } else {
                this.endIndex += charsRead;
            }
        } catch (IOException e) {
            throw new ParserException(
                    "ScriptTokenizerInputBuffer", "ERROR_FILL_BUFFER",                    
                    "Error reading data into ScriptTokenizerInputBuffer", e);
        }
    }

    public Token token(){
        Token token = this.tokenPool.take();
        token.setBuffer(this.buffer);
        return token;
    }

    public ParserMark mark(){
        ParserMark mark = new ParserMark();
        mark.markIndex = this.index - this.factoryStartIndex;
        return mark;
    }

    public void backtrackTo(ParserMark mark){
        int absoluteMarkIndex = this.factoryStartIndex + mark.markIndex;
        int charsToBacktrack = this.index - absoluteMarkIndex;
        this.index -= charsToBacktrack;

    }

    public boolean isEndOfInputReached(){
        return (this.index == this.endIndex) && this.endOfStreamReached;
    }

}
