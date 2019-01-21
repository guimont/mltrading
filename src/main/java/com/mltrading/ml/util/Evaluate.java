package com.mltrading.ml.util;

import com.mltrading.ml.*;
import com.mltrading.ml.model.GradiantBoostStock;
import com.mltrading.ml.model.ModelType;
import com.mltrading.ml.model.RandomForestStock;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockSector;
import com.mltrading.models.stock.cache.CacheStockGeneral;
import com.mltrading.models.stock.cache.CacheStockSector;
import com.mltrading.models.util.CsvFileReader;
import com.mltrading.models.util.CsvFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gmo on 19/06/2017.
 */
public abstract class Evaluate {

    private static final Logger log = LoggerFactory.getLogger(Evaluate.class);


    public int getRowSector(String codif) {
        int rowSector = CacheStockSector.NO_SECTOR;
        try {
            StockGeneral sg = CacheStockGeneral.getCache().get(CacheStockGeneral.getCode(codif));
            String sector = sg.getSector();
            if (sector != null)
                rowSector = CacheStockSector.getSectorCache().get(sector).getRow();
        } catch (Exception e) {
            log.error("Cannot find sector for: "+ codif);
        }

        return rowSector;
    }


    /**
     * function to export matrix validator model to csv file
     * it a backup model to regenerate model from file
     */
    public void exportModel() {
        CsvFileWriter fileWriter = new CsvFileWriter("MatrixValidator.csv", "");

        List<StockGeneral> l = new ArrayList<>(CacheStockGeneral.getIsinCache().values());


        for (StockGeneral s : l) {
            MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
            if (mls != null && mls.isEmtpyModel() == false) {
                PeriodicityList.periodicityLong.forEach(p -> mls.getModel(p).export(fileWriter,s.getCodif(),p.toString()));
            }
        }

        List<StockSector> ls = new ArrayList<>(CacheStockSector.getSectorCache().values());

        for (StockSector s : ls) {
            MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
            if (mls != null && mls.isEmtpyModel() == false) {
                PeriodicityList.periodicityLong.forEach(p -> mls.getModel(p).export(fileWriter,s.getCodif(),p.toString()));
            }
        }


        fileWriter.close();
    }

    /**
     *
     */
    public void importModel() {


        try {
            HashMap<String, MatrixValidator> mapMV = new HashMap<>();
            new CsvFileReader("MatrixValidator.csv",mapMV);



            List<StockGeneral> l = new ArrayList<>(CacheStockGeneral.getIsinCache().values());

            /**
             * Start RANDOMFOREST import
             */
            for (StockGeneral s : l) {


                final MLStocks mls = new MLStocksBase(s.getCodif());

                if (mapMV.get(s.getCodif() + ModelType.code(ModelType.RANDOMFOREST) + PredictionPeriodicity.D20) == null)
                    continue;

                PeriodicityList.periodicityLong.forEach(p -> {
                    MatrixValidator mv = mapMV.get(s.getCodif() + ModelType.code(ModelType.RANDOMFOREST) + p);
                    mls.createValidator(p, mv, ModelType.RANDOMFOREST);
                    mls.getSock(p).setModelImprove(true);
                });

                RandomForestStock rfs = new RandomForestStock();
                rfs.processSpecifcRFRef(s.getCodif(), mls);
                if (mls.getStatus(ModelType.RANDOMFOREST).getPerfList() != null) {
                    mls.getStatus(ModelType.RANDOMFOREST).calculeAvgPrd();
                    CacheMLStock.getMLStockCache().put(s.getCodif(), mls);
                    PeriodicityList.periodicityLong.forEach(p -> {
                        mls.saveModel(p, ModelType.RANDOMFOREST);
                        mls.saveDB(p, ModelType.RANDOMFOREST);
                    });
                    mls.getStatus(ModelType.RANDOMFOREST).savePerf(mls.getCodif(), ModelType.RANDOMFOREST, mls.getDbNamePerf());
                    mls.saveValidator(ModelType.RANDOMFOREST);
                }
            }
            /**
             * end RANDOMFOREST import
             */

            /**
             * Start GRADIANTBOOSTTREE import
             */

            //boolean jump = true;
            for (StockGeneral s : l) {

                final MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());

                /*if (s.getCodif().equalsIgnoreCase("ENGI")) jump = false;
                if (jump) continue;*/

                /** cannot filled so RF is not valid*/
                /** skip this model GBT so           */
                if (mls == null) {
                    continue;
                }

                if (mapMV.get(s.getCodif() + ModelType.code(ModelType.GRADIANTBOOSTTREE) + PredictionPeriodicity.D20) == null)
                    continue;

                PeriodicityList.periodicityLong.forEach(p -> {
                    MatrixValidator mv = mapMV.get(s.getCodif() + ModelType.code(ModelType.GRADIANTBOOSTTREE) + p);
                    mls.createValidator(p, mv, ModelType.GRADIANTBOOSTTREE);
                    mls.getSock(p).setModelImprove(true);
                });

                GradiantBoostStock gbt = new GradiantBoostStock();
                gbt.processSpecifcRFRef(s.getCodif(),mls);
                if (mls.getStatus(ModelType.GRADIANTBOOSTTREE).getPerfList() != null) {
                    mls.getStatus(ModelType.GRADIANTBOOSTTREE).calculeAvgPrd();
                    CacheMLStock.getMLStockCache().put(s.getCodif(), mls);
                    PeriodicityList.periodicityLong.forEach(p -> {
                        mls.saveModel(p, ModelType.GRADIANTBOOSTTREE);
                        mls.saveDB(p, ModelType.GRADIANTBOOSTTREE);
                    });
                    mls.getStatus(ModelType.GRADIANTBOOSTTREE).savePerf(mls.getCodif(), ModelType.GRADIANTBOOSTTREE, mls.getDbNamePerf());
                    mls.saveValidator(ModelType.GRADIANTBOOSTTREE);
                }

            }
            /**
             * end GRADIANTBOOSTTREE import
             */

            for (StockGeneral s : l) {

                final MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());
                updateEnsemble();
                mls.getStatus(ModelType.ENSEMBLE).savePerf(mls.getCodif(), ModelType.ENSEMBLE, mls.getDbNamePerf());
            }


            List<StockSector> ls = new ArrayList<>(CacheStockSector.getSectorCache().values());

            /**
             * Start RANDOMFOREST import
             */
            for (StockSector s : ls) {

                final MLStocks mls = new MLStocksBase(s.getCodif());

                PeriodicityList.periodicityLong.forEach(p -> {
                    MatrixValidator mv = mapMV.get(s.getCodif() + ModelType.code(ModelType.RANDOMFOREST) + p);
                    mls.createValidator(p, mv, ModelType.RANDOMFOREST);
                    mls.getSock(p).setModelImprove(true);
                });

                RandomForestStock rfs = new RandomForestStock();
                rfs.processSpecifcRFRef(s.getCodif(), mls);
                if (mls.getStatus(ModelType.RANDOMFOREST).getPerfList() != null) {
                    mls.getStatus(ModelType.RANDOMFOREST).calculeAvgPrd();
                    CacheMLStock.getMLStockCache().put(s.getCodif(), mls);
                    PeriodicityList.periodicityLong.forEach(p -> {
                        mls.saveModel(p, ModelType.RANDOMFOREST);
                        mls.saveDB(p, ModelType.RANDOMFOREST);
                    });
                    mls.getStatus(ModelType.RANDOMFOREST).savePerf(mls.getCodif(), ModelType.RANDOMFOREST,  mls.getDbNamePerf());
                    mls.saveValidator(ModelType.RANDOMFOREST);
                }
            }

            /**
             * end RANDOMFOREST import
             */

            /**
             * Start GRADIANTBOOSTTREE import
             */
            for (StockSector s : ls) {

                final MLStocks mls = CacheMLStock.getMLStockCache().get(s.getCodif());

                /** cannot filled so RF is not valid*/
                /** skip this model GBT so           */
                if (mls == null) {
                    continue;
                }

                PeriodicityList.periodicityLong.forEach(p -> {

                    MatrixValidator mv = mapMV.get(s.getCodif() + ModelType.code(ModelType.GRADIANTBOOSTTREE) + p);
                    mls.createValidator(p, mv, ModelType.GRADIANTBOOSTTREE);
                    mls.getSock(p).setModelImprove(true);
                });

                GradiantBoostStock gbt = new GradiantBoostStock();
                gbt.processSpecifcRFRef(s.getCodif(),mls);
                if (mls.getStatus(ModelType.GRADIANTBOOSTTREE).getPerfList() != null) {
                    mls.getStatus(ModelType.GRADIANTBOOSTTREE).calculeAvgPrd();
                    CacheMLStock.getMLStockCache().put(s.getCodif(), mls);
                    PeriodicityList.periodicityLong.forEach(p -> {
                        mls.saveModel(p, ModelType.GRADIANTBOOSTTREE);
                        mls.saveDB(p, ModelType.GRADIANTBOOSTTREE);
                    });
                    mls.getStatus(ModelType.GRADIANTBOOSTTREE).savePerf(mls.getCodif(), ModelType.GRADIANTBOOSTTREE, mls.getDbNamePerf());
                    mls.saveValidator(ModelType.GRADIANTBOOSTTREE);
                }

            }
            /**
             * end GRADIANTBOOSTTREE import
             */

        } catch (Exception e) {
            log.error(e.toString());
        }


    }

    public abstract void updateEnsemble();

}
