package com.mltrading.ml;

import com.mltrading.models.stock.CacheRawMaterial;
import com.mltrading.models.stock.CacheStockIndice;
import com.mltrading.models.stock.CacheStockSector;

/**
 * Created by gmo on 21/07/2016.
 */
public class MatrixValidator {

    Integer matrix[][];

    static private int HS_POS = 0;

    static private int N_HS = 1;

    int global = CacheStockSector.N_SECTOR+ CacheStockIndice.N_INDICE+ CacheRawMaterial.N_RAW+ N_HS;

    /**
     * col indice
     */

    static private int COL_PERIOD = 0;


    public MatrixValidator() {

    }

    void generate() {
        for (int i =0 ; i < global; i++)
            matrix[i][COL_PERIOD] = Validator.randomPeriod(2, 50);
        /*
                for (j=0; j< N_AT; j++)
                    matrix[i][j] = Validator.randomBoll()

         */
    }

}
