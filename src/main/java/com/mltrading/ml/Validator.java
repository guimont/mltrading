package com.mltrading.ml;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

/**
 * Created by gmo on 14/02/2016.
 */
public class Validator {
    Integer maxDepth = 6;
    Integer maxBins = 64;
    Integer numTrees = 100; // Use more in practice.
    Integer seed = 12345;


    public void generate() {
        maxDepth = (int) (Math.random()*(64-2));
        maxBins = (int) (Math.random()*(200-1));
        numTrees = (int) (Math.random()*(400-5));
        seed = (int) (Math.random()*(20000-5));
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

    public void save(String code, double error) {
        BatchPoints bp = InfluxDaoConnector.getBatchPoints();

        Point pt = Point.measurement(code)
            .field("maxDepth", maxDepth)
            .field("maxBins",maxBins)
            .field("numTrees",numTrees)
            .field("seed",seed)
            .field("error",error)
            .build();
        bp.point(pt);

        InfluxDaoConnector.writePoints(bp);

    }

}
