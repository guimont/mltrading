package com.mltrading.ml;

import com.mltrading.models.stock.CacheRawMaterial;
import com.mltrading.models.stock.CacheStockIndice;
import com.mltrading.models.stock.CacheStockSector;
import com.mltrading.models.stock.StockAnalyse;

/**
 * Created by gmo on 21/07/2016.
 */
public class MatrixValidator {

    Integer matrix[][];

    static private int HS_POS = 0;
    static private int N_HS = 1;

    private int globalROW = CacheStockSector.N_SECTOR+ CacheStockIndice.N_INDICE+ CacheRawMaterial.N_RAW+ N_HS;
    private int globalCOL = N_HS+StockAnalyse.N_AT;

    /**
     * col indice
     */

    static private int COL_PERIOD = 0;


    public MatrixValidator() {
        matrix = new Integer[globalROW][globalCOL];
    }

    public void generate() {
        for (int i = 0 ; i < globalROW; i++) {
            matrix[i][COL_PERIOD] = Validator.randomPeriod(2, 50);
            for (int j = 0; j < StockAnalyse.N_AT; j++)
                matrix[i][j] = Validator.randomiBool();
        }
    }

}
