package com.mltrading.asset;

import akka.japi.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public  void logic() {
        boolean a = true, b= true;
        boolean c = false, d = true;


        assertThat((a && b) || (c && d)).isTrue();

        List <String> listA = new ArrayList<>();
        listA.add("toto");
        List <String> listB = null;


        assertThat(((listA == null || listB == null)
            || (listA!= null && listA.size() == 0 || listB!= null && listB.size() ==0))).isTrue();


        listB = new ArrayList<>();
        listB.add("toto");

        assertThat(((listA == null || listB == null)
            || (listA!= null && listA.size() == 0 || listB!= null && listB.size() ==0))).isFalse();
    }

}
