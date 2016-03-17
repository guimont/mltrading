package com.mltrading.models.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by gmo on 14/03/2016.
 */
public class HistogramDocument {

    private static final Logger log = LoggerFactory.getLogger(HistogramDocument.class);

    private String code;
    private String day;
    private int lvl_L4 = 0;
    private int lvl_L3 = 0;
    private int lvl_L2 = 0;
    private int lvl_L1 = 0;
    private int lvl_0 = 0;
    private int lvl_P1 = 0;
    private int lvl_P2 = 0;
    private int lvl_P3 = 0;
    private int lvl_P4 = 0;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getLvl_L4() {
        return lvl_L4;
    }

    public void setLvl_L4(int lvl_L4) {
        this.lvl_L4 = lvl_L4;
    }

    public int getLvl_L3() {
        return lvl_L3;
    }

    public void setLvl_L3(int lvl_L3) {
        this.lvl_L3 = lvl_L3;
    }

    public int getLvl_L2() {
        return lvl_L2;
    }

    public void setLvl_L2(int lvl_L2) {
        this.lvl_L2 = lvl_L2;
    }

    public int getLvl_L1() {
        return lvl_L1;
    }

    public void setLvl_L1(int lvl_L1) {
        this.lvl_L1 = lvl_L1;
    }

    public int getLvl_0() {
        return lvl_0;
    }

    public void setLvl_0(int lvl_0) {
        this.lvl_0 = lvl_0;
    }

    public int getLvl_P1() {
        return lvl_P1;
    }

    public void setLvl_P1(int lvl_P1) {
        this.lvl_P1 = lvl_P1;
    }

    public int getLvl_P2() {
        return lvl_P2;
    }

    public void setLvl_P2(int lvl_P2) {
        this.lvl_P2 = lvl_P2;
    }

    public int getLvl_P3() {
        return lvl_P3;
    }

    public void setLvl_P3(int lvl_P3) {
        this.lvl_P3 = lvl_P3;
    }

    public int getLvl_P4() {
        return lvl_P4;
    }

    public void setLvl_P4(int lvl_P4) {
        this.lvl_P4 = lvl_P4;
    }

    private void setHisto(int note) {
        switch (note) {
            case -4:
                lvl_L4++;
                break;
            case -3:
                lvl_L3++;
                break;
            case -2:
                lvl_L2++;
                break;
            case -1:
                lvl_L1++;
                break;

            case 4:
                lvl_P4++;
                break;
            case 3:
                lvl_P3++;
                break;
            case 2:
                lvl_P2++;
                break;
            case 1:
                lvl_P1++;
                break;
        }
    }

    private String lem(String w) {
        String r  = w;
        if (r.endsWith("s")) r  = r.substring(0,r.length()-1);
        if (r.endsWith("ée")) r = r.substring(0,r.length()-1);

        return r;
    }


    public void test() {
        String test = lem ("infligées");
        System.out.println(test);
    }

    public void analyseDocument(String doc, Map<String, Integer> dico) {
        String[] l = doc.split(" ");

        for (String w:l) {
            Integer note = dico.get(lem(w));
            if (note != null) {
                log.info("word find: " + w + " for note: " + note);
                setHisto(note.intValue());
            }
        }



    }

    public int sum() {
        return lvl_L4*-4 + lvl_L3*-3 + lvl_L2*-2 + lvl_L1*-1 + lvl_0 + lvl_P1 + lvl_P2*2 + lvl_P3*3 + lvl_P4*4;
    }
}
