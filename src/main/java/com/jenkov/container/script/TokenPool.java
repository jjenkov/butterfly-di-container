package com.jenkov.container.script;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * This pool should make sure that Token instances are reused throughout the parser.
 * This is done to avoid using String tokens, which apparently take up a lot of memory
 * during parsing, when many String tokens are created. To avoid this, tokens are reused
 * via this pool.
 */
public class TokenPool {

    protected final Deque<Token> free = new ArrayDeque<>();
    protected final List<Token> taken = new ArrayList<>();

    protected int tokensTaken = 0;
    protected int maxSize = 0;


    public Token take() {
        this.tokensTaken++;
        Token token = free.poll();
        if (token!=null) {
            token.reset();
        } else {
            token = new Token();
        }
        taken.add(token);

        if (size() > maxSize) {
            maxSize = size();
        }

        return token;
    }

    public void freeAll() {
        free.addAll(taken);
        taken.clear();
    }

    public void correctIndexOfTakenTokens(int valueToSubstract) {
        List<Token> taken1 = this.taken;
        for (int i = 0, taken1Size = taken1.size(); i < taken1Size; i++) {
            Token token = taken1.get(i);
            if (token.length > 0) {
                token.from -= valueToSubstract;
            }
        }
    }

    public int size() {
        return free.size() + taken.size();
    }


}
