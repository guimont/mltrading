package com.mltrading.ml.util;

import com.mltrading.ml.MatrixValidator;

import java.util.Random;

/**
 * Created by gmo on 15/06/2017.
 */




public class Combination {

    private static final Random random = new Random(0);

    MatrixValidator mv;

    public static Combination newInstance() {
        return new Combination();
    }



    public Combination() {
        mv = new MatrixValidator();
    }

    public int evaluate() {

        //inverse score
        return 300;
    }



    public Combination merge(Combination other) {
        return new Combination();
    }

    public Combination mutate() {
        return new Combination();
    }

    private boolean randomBoolean() {
        return random.nextBoolean();
    }

}
