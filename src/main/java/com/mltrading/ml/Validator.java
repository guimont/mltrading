package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 14/02/2016.
 */
public class Validator {
    Integer maxDepth = 3;
    Integer maxBins = 32;
    Integer numTrees = 100; // Use more in practice.
    Integer seed = 12345;

    public int perdiodHist = 20;
    public boolean historyAT = true;
    public boolean historyConsensus  = true;
    public int perdiodSector = 20;
    public boolean sectorAT = true;
    public int perdiodCac = 20;
    public boolean cac = true;
    public boolean cacAT = true;
    public boolean indiceDJI = true;
    public int perdiodDJI = 20;
    public boolean indiceN225 = true;
    public int perdiodN225 = 20;
    public boolean indiceFTSE = true;
    public int perdiodFTSE = 20;
    public boolean cacVola = true;
    public int perdiodcacVola = 20;

    public boolean historyVolume = true;
    public boolean analyseMme12 = true;
    public boolean analyseMme26 = true;
    public boolean analyseMomentum = true;
    public boolean analyseStdDev = true;

    public boolean DJIAT = true;
    public boolean N225AT = true;
    public boolean FTSEAT = true;





    public boolean randomBool() {
        if (Math.random()>0.5) return true;
        return false;
    }

    public int randomPeriod() {
        return (int) (Math.random() * 40 + 2);
    }

    public void generateFeature() {
        perdiodHist = randomPeriod();
        historyAT = randomBool();
        historyConsensus  =  randomBool();
        perdiodSector = randomPeriod();
        sectorAT =  randomBool();
        perdiodCac = randomPeriod();
        cac =  randomBool();
        cacAT =  randomBool();
        indiceDJI =  randomBool();
        perdiodDJI = randomPeriod();
        DJIAT =  randomBool();
        indiceN225 =  randomBool();
        perdiodN225 = randomPeriod();
        N225AT = randomBool();
        indiceFTSE =  randomBool();
        perdiodFTSE = randomPeriod();
        FTSEAT = randomBool();
        cacVola =  randomBool();
        perdiodcacVola = randomPeriod();

        historyVolume = randomBool();
        analyseMme12 = randomBool();
        analyseMme26 = randomBool();
        analyseMomentum = randomBool();
        analyseStdDev = randomBool();
    }

    public void generate() {
        maxDepth = (int) (Math.random()*(28) +2 );
        maxBins = (int) (Math.random()*(200) + 2);
        numTrees = (int) (Math.random()*(400) +1);
        seed = (int) (Math.random()*(20000) +1);
    }

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    public Integer getMaxBins() {
        return maxBins;
    }

    public void setMaxBins(Integer maxBins) {
        this.maxBins = maxBins;
    }

    public Integer getNumTrees() {
        return numTrees;
    }

    public void setNumTrees(Integer numTrees) {
        this.numTrees = numTrees;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public void save(String code, int error) {
        BatchPoints bp = InfluxDaoConnector.getBatchPoints();

        Point pt = Point.measurement(code + 'V')
            .field("maxDepth", maxDepth)
            .field("maxBins",maxBins)
            .field("numTrees", numTrees)
            .field("seed",seed)

            .field("perdiodHist", perdiodHist)
            .field("historyAT",historyAT)
            .field("historyConsensus",historyConsensus)
            .field("perdiodSector", perdiodSector)
            .field("sectorAT",sectorAT)
            .field("perdiodCac",perdiodCac)
            .field("cac",cac)
            .field("cacAT",cacAT)
            .field("indiceDJI", indiceDJI)
            .field("perdiodDJI",perdiodDJI)
            .field("indiceN225",indiceN225)
            .field("perdiodN225",perdiodN225)
            .field("indiceFTSE",indiceFTSE)
            .field("perdiodFTSE",perdiodFTSE)
            .field("cacVola", cacVola)
            .field("perdiodcacVola", seed)

            .field("historyVolume", historyVolume)
            .field("analyseMme12",analyseMme12)
            .field("analyseMme26",analyseMme26)
            .field("analyseMomentum",analyseMomentum)
            .field("analyseStdDev",analyseStdDev)
            .field("DJIAT",DJIAT)
            .field("N225AT",N225AT)
            .field("FTSEAT",FTSEAT)

            .field("error",error)
            .build();
        bp.point(pt);

        InfluxDaoConnector.writePoints(bp);

    }

}
