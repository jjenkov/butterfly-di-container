package com.jenkov.container.script;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class ObjectPool<T> {

    protected final List<T> free = new ArrayList<>();
    protected final List<T> taken = new ArrayList<>();

    protected int tokensTaken = 0;
    protected int maxSize = 0;


    public T take() {
        this.tokensTaken++;
        T token = null;
        if (free.size() > 0) {
            token = free.remove(0);
        } else {
            // token =
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
//        for(T token : this.taken){
//            if(token.length > 0){
//                token.from -= valueToSubstract;
//            }
//        }
    }

    public int size() {
        return free.size() + taken.size();
    }


}
