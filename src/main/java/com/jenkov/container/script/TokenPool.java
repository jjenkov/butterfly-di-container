package com.jenkov.container.script;

import java.util.List;
import java.util.ArrayList;

/**
  This pool should make sure that Token instances are reused throughout the parser.
  This is done to avoid using String tokens, which apparently take up a lot of memory
  during parsing, when many String tokens are created. To avoid this, tokens are reused
  via this pool.
 */
public class TokenPool {

    protected List<Token> free  = new ArrayList<Token>();
    protected List<Token> taken = new ArrayList<Token>();

    protected int tokensTaken = 0;
    protected int maxSize     = 0;


    public Token take(){
        this.tokensTaken++;
        Token token = null;
        if(free.size() > 0) {
            token = free.remove(0);
            token.reset();
        } else {
            token = new Token();
        }
        taken.add(token);

        if(size() > maxSize){
            maxSize = size();
        }
        
        return token;
    }

    public void freeAll(){
        free.addAll(taken);
        taken.clear();
    }
    
    public void correctIndexOfTakenTokens(int valueToSubstract){
        for(Token token : this.taken){
            if(token.length > 0){
                token.from -= valueToSubstract;
            }
        }
    }

    public int size() {
        return free.size() + taken.size();
    }


}
