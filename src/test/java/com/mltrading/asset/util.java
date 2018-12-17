package com.mltrading.asset;

import akka.japi.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class util {

    @Test
    public void processTest() {
        List<Pair<Double, String>> predState = new ArrayList();
        predState.add(new Pair<>(2.,"toto"));
        predState.add(new Pair<>(2.3,"toto"));
        predState.add(new Pair<>(2.8,"toto"));
        predState.add(new Pair<>(4.,"toto"));
        predState.add(new Pair<>(-2.,"toto"));
        predState.add(new Pair<>(29.,"toto"));
        predState.sort(Collections.reverseOrder(Comparator.comparingDouble(Pair::first)));
    }

}
