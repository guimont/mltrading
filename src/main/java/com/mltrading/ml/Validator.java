package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.dao.InfluxDaoConnectorPerf;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;
import com.mltrading.influxdb.dto.QueryResult;

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

    public boolean indiceDAX = true;
    public int perdiodDAX = 20;
    public boolean indiceSTOXX50 = true;
    public int perdiodSTOXX50= 20;
    public boolean DOLLAR = true;
    public int perdiodDOLLAR = 20;
    public boolean PETROL = true;
    public int perdiodPETROL = 20;


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
    public boolean DAXIAT = true;
    public boolean STOXX50AT = true;

    public boolean EURI1M = true;
    public int perdiodEURI1M = 20;
    public boolean EURI1Y = true;
    public int perdiodEURI1Y = 20;
    public boolean EURI10Y = true;
    public int perdiodEURI10Y = 20;

    public boolean USRI1M = true;
    public int perdiodUSRI1M = 20;
    public boolean USRI1Y = true;
    public int perdiodUSRI1Y = 20;
    public boolean USRI10Y = true;
    public int perdiodUSRI10Y = 20;




    public void loadValidator(String code) {
        String query = "SELECT min(error) FROM " + code;
        QueryResult result = InfluxDaoConnector.getPoints(query);

        double minError = (double) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(1);

        query = "SELECT min(rate) FROM " + code+ " where error="+ minError ;
        result = InfluxDaoConnector.getPoints(query);

        double minRate = (double) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(1);

        query = "SELECT * FROM " + code + " where error="+ minError +" and rate="+minRate;
        result = InfluxDaoConnector.getPoints(query);

        this.DJIAT = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(1);
        this.FTSEAT = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(2);
        this.N225AT = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(3);
        this.analyseMme12 = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(4);
        this.analyseMme26 = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(5);
        this.analyseMomentum = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(6);
        this.analyseStdDev = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(7);
        //async = result.getResults().get(0).getSeries().get(0).getValues().get(0).get(8);
        this.cac = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(9);
        this.cacAT = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(10);
        this.cacVola = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(11);
        //error = result.getResults().get(0).getSeries().get(0).getValues().get(0).get(12);
        this.historyAT = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(13);
        this.historyConsensus = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(14);
        this.historyVolume = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(15);
        this.indiceDJI = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(16);
        this.indiceFTSE = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(17);
        this.indiceN225 = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(18);
        this.maxBins = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(19))).intValue());
        this.maxDepth = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(20))).intValue());
        this.numTrees = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(21))).intValue());
        this.perdiodCac = ((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(22))).intValue();
        this.perdiodDJI = ((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(23))).intValue();
        this.perdiodFTSE = ((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(24))).intValue();
        this.perdiodHist = ((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(25))).intValue();
        this.perdiodN225 = ((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(26))).intValue();
        this.perdiodSector = ((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(27))).intValue();
        this.perdiodcacVola = ((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(28))).intValue();
        //rate = result.getResults().get(0).getSeries().get(0).getValues().get(0).get(29);
        this.sectorAT = (boolean) result.getResults().get(0).getSeries().get(0).getValues().get(0).get(30);
        this.seed = new Integer(((Double)(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(31))).intValue());


    }



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
        DAXIAT = randomBool();
        indiceDAX = randomBool();
        perdiodDAX = randomPeriod();
        STOXX50AT  = randomBool();
        indiceSTOXX50 = randomBool();
        perdiodSTOXX50= randomPeriod();
        DOLLAR = randomBool();
        perdiodDOLLAR = randomPeriod();
        PETROL = randomBool();
        perdiodPETROL = randomPeriod();

        EURI1M = randomBool();
        perdiodEURI1M = randomPeriod();
        EURI1Y = randomBool();
        perdiodEURI1Y = randomPeriod();
        EURI10Y = randomBool();
        perdiodEURI10Y = randomPeriod();

        USRI1M = randomBool();
        perdiodUSRI1M = randomPeriod();
        USRI1Y = randomBool();
        perdiodUSRI1Y = randomPeriod();
        USRI10Y = randomBool();
        perdiodUSRI10Y = randomPeriod();

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

    public void save(String code, int error, double rate) {
        BatchPoints bp = InfluxDaoConnectorPerf.getBatchPoints();

        Point pt = Point.measurement(code)
            .field("maxDepth", maxDepth)
            .field("maxBins", maxBins)
            .field("numTrees", numTrees)
            .field("seed", seed)

            .field("perdiodHist", perdiodHist)
            .field("historyAT", historyAT)
            .field("historyConsensus", historyConsensus)
            .field("perdiodSector", perdiodSector)
            .field("sectorAT", sectorAT)
            .field("perdiodCac",perdiodCac)
            .field("cac", cac)
            .field("cacAT",cacAT)
            .field("indiceDJI", indiceDJI)
            .field("perdiodDJI", perdiodDJI)
            .field("indiceN225",indiceN225)
            .field("perdiodN225", perdiodN225)
            .field("indiceFTSE", indiceFTSE)
            .field("perdiodFTSE",perdiodFTSE)
            .field("cacVola", cacVola)
            .field("perdiodcacVola", perdiodcacVola)

            .field("DAXIAT", DAXIAT)
            .field("indiceDAX", indiceDAX)
            .field("perdiodDAX", perdiodDAX)
            .field("STOXX50AT",STOXX50AT)
            .field("indiceSTOXX50",indiceSTOXX50)
            .field("perdiodSTOXX50", perdiodSTOXX50)
            .field("DOLLAR", DOLLAR)
            .field("perdiodDOLLAR", perdiodDOLLAR)
            .field("PETROL", DOLLAR)
            .field("perdiodPETROL", perdiodDOLLAR)

            .field("historyVolume", historyVolume)
            .field("analyseMme12", analyseMme12)
            .field("analyseMme26", analyseMme26)
            .field("analyseMomentum",analyseMomentum)
            .field("analyseStdDev", analyseStdDev)
            .field("DJIAT",DJIAT)
            .field("N225AT", N225AT)
            .field("FTSEAT",FTSEAT)

            .field("EURI1M", EURI1M)
            .field("perdiodEURI1M", perdiodEURI1M)
            .field("EURI1Y", EURI1Y)
            .field("perdiodEURI1Y", perdiodEURI1Y)
            .field("EURI10Y", EURI10Y)
            .field("perdiodEURI10Y", perdiodEURI10Y)

            .field("USRI1M", USRI1M)
            .field("perdiodUSRI1M", perdiodUSRI1M)
            .field("USRI1Y", USRI1Y)
            .field("perdiodUSRI1Y", perdiodUSRI1Y)
            .field("USRI10Y", USRI10Y)
            .field("perdiodUSRI10Y", perdiodUSRI10Y)

            .field("error", error)
            .field("rate", rate)
            .build();
        bp.point(pt);

        InfluxDaoConnectorPerf.writePoints(bp);

    }

}
