package com.mltrading.models.stock;

/**
 * Created by gmo on 23/03/2017.
 */

import com.mltrading.ml.MatrixValidator;
import com.mltrading.ml.PredictionPeriodicity;
import com.mltrading.models.stock.cache.CacheRawMaterial;
import com.mltrading.models.stock.cache.CacheStockIndice;
import com.mltrading.models.stock.cache.CacheStockSector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * explain validator model with interface
 * dont use for processing algorithm
 */
public class StockValidator implements Serializable{

    String code;
    String period;

    Integer matrix[];
    String label[];

    public StockValidator() {
        matrix = new Integer[MatrixValidator.globalROW];
        label = new String[MatrixValidator.globalROW];
    }


    public Integer[] getMatrix() {
        return matrix;
    }

    public void setMatrix(Integer[] matrix) {
        this.matrix = matrix;
    }

    public String[] getLabel() {
        return label;
    }

    public void setLabel(String[] label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * Filled object with matrix
     * order of matrix follow definition
     * CacheStockSector.N_SECTOR
     * CacheStockIndice.N_INDICE
     * CacheRawMaterial.N_RAW
     * @param key
     * @param pKey
     * @param mv
     */
    static int EMPTY = 0;
    public StockValidator filled(String key, PredictionPeriodicity pKey, MatrixValidator mv) {

        this.setCode(key);
        this.setPeriod(pKey.toString());


        int index = filled(new ArrayList(CacheStockSector.getSectorCache().values()), mv, MatrixValidator.TypeHistory.SEC, 0);
        index = filled(new ArrayList(CacheStockIndice.getIndiceCache().values()), mv, MatrixValidator.TypeHistory.IND, index);
        filled(new ArrayList(CacheRawMaterial.getCache().values()), mv, MatrixValidator.TypeHistory.RAW, index);


        return this;

    }


    private int filled(List<? extends StockHistory> list, MatrixValidator mv,MatrixValidator.TypeHistory offsetType, int index) {
        for (StockHistory g : list) {
            int row = mv.getIndice(g.getRow(), offsetType);
            if (mv.getPeriodEnable(row)) {
                matrix[index] = mv.getPeriodHist(row);
            }
            else {
                matrix[index] = EMPTY;
            }
            label[index++] = g.getCode();
        }
        return index;
    }



}
