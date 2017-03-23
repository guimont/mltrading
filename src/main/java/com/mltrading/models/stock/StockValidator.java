package com.mltrading.models.stock;

/**
 * Created by gmo on 23/03/2017.
 */

import com.mltrading.ml.MatrixValidator;
import com.mltrading.ml.PredictionPeriodicity;
import com.mltrading.models.stock.cache.CacheStockSector;

import java.io.Serializable;

/**
 * explain validator model with interface
 * dont use for processing algorithm
 */
public class StockValidator implements Serializable{

    String code;
    String period;

    int sectorFRIN;
    int sectorFRBM;
    int sectorFROGP;
    int sectorFRCG;
    int sectorFRHC;
    int sectorFRCS;
    int sectorFRTEL;
    int sectorFRUT;
    int sectorFRFIN;
    int sectorFRTEC;

    int indicePX1;
    int indicePX4;
    int indiceFTSE;
    int indiceSMI;
    int indiceDJI;
    int indiceINX;
    int indiceNDX;
    int indiceNI225;
    int indiceHSI;
    int indiceSENSEX;


    public int getSectorFRIN() {
        return sectorFRIN;
    }

    public void setSectorFRIN(int sectorFRIN) {
        this.sectorFRIN = sectorFRIN;
    }

    public int getSectorFRBM() {
        return sectorFRBM;
    }

    public void setSectorFRBM(int sectorFRBM) {
        this.sectorFRBM = sectorFRBM;
    }

    public int getSectorFROGP() {
        return sectorFROGP;
    }

    public void setSectorFROGP(int sectorFROGP) {
        this.sectorFROGP = sectorFROGP;
    }

    public int getSectorFRCG() {
        return sectorFRCG;
    }

    public void setSectorFRCG(int sectorFRCG) {
        this.sectorFRCG = sectorFRCG;
    }

    public int getSectorFRHC() {
        return sectorFRHC;
    }

    public void setSectorFRHC(int sectorFRHC) {
        this.sectorFRHC = sectorFRHC;
    }

    public int getSectorFRCS() {
        return sectorFRCS;
    }

    public void setSectorFRCS(int sectorFRCS) {
        this.sectorFRCS = sectorFRCS;
    }

    public int getSectorFRTEL() {
        return sectorFRTEL;
    }

    public void setSectorFRTEL(int sectorFRTEL) {
        this.sectorFRTEL = sectorFRTEL;
    }

    public int getSectorFRUT() {
        return sectorFRUT;
    }

    public void setSectorFRUT(int sectorFRUT) {
        this.sectorFRUT = sectorFRUT;
    }

    public int getSectorFRFIN() {
        return sectorFRFIN;
    }

    public void setSectorFRFIN(int sectorFRFIN) {
        this.sectorFRFIN = sectorFRFIN;
    }

    public int getSectorFRTEC() {
        return sectorFRTEC;
    }

    public void setSectorFRTEC(int sectorFRTEC) {
        this.sectorFRTEC = sectorFRTEC;
    }

    public int getIndicePX1() {
        return indicePX1;
    }

    public void setIndicePX1(int indicePX1) {
        this.indicePX1 = indicePX1;
    }

    public int getIndicePX4() {
        return indicePX4;
    }

    public void setIndicePX4(int indicePX4) {
        this.indicePX4 = indicePX4;
    }

    public int getIndiceFTSE() {
        return indiceFTSE;
    }

    public void setIndiceFTSE(int indiceFTSE) {
        this.indiceFTSE = indiceFTSE;
    }

    public int getIndiceSMI() {
        return indiceSMI;
    }

    public void setIndiceSMI(int indiceSMI) {
        this.indiceSMI = indiceSMI;
    }

    public int getIndiceDJI() {
        return indiceDJI;
    }

    public void setIndiceDJI(int indiceDJI) {
        this.indiceDJI = indiceDJI;
    }

    public int getIndiceINX() {
        return indiceINX;
    }

    public void setIndiceINX(int indiceINX) {
        this.indiceINX = indiceINX;
    }

    public int getIndiceNDX() {
        return indiceNDX;
    }

    public void setIndiceNDX(int indiceNDX) {
        this.indiceNDX = indiceNDX;
    }

    public int getIndiceNI225() {
        return indiceNI225;
    }

    public void setIndiceNI225(int indiceNI225) {
        this.indiceNI225 = indiceNI225;
    }

    public int getIndiceHSI() {
        return indiceHSI;
    }

    public void setIndiceHSI(int indiceHSI) {
        this.indiceHSI = indiceHSI;
    }

    public int getIndiceSENSEX() {
        return indiceSENSEX;
    }

    public void setIndiceSENSEX(int indiceSENSEX) {
        this.indiceSENSEX = indiceSENSEX;
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
    public StockValidator filled(String key, PredictionPeriodicity pKey, MatrixValidator mv) {

        this.setCode(key);
        this.setPeriod(pKey.toString());

        this.setSectorFRIN(getValue(mv, CacheStockSector.SECTOR_FRIN_POS, MatrixValidator.N_HS));
        this.setSectorFRBM(getValue(mv, CacheStockSector.SECTOR_FRBM_POS, MatrixValidator.N_HS));
        this.setSectorFRCG(getValue(mv, CacheStockSector.SECTOR_FRCG_POS, MatrixValidator.N_HS));
        this.setSectorFRCS(getValue(mv, CacheStockSector.SECTOR_FRCS_POS, MatrixValidator.N_HS));
        this.setSectorFRFIN(getValue(mv, CacheStockSector.SECTOR_FRFIN_POS, MatrixValidator.N_HS));
        this.setSectorFRHC(getValue(mv, CacheStockSector.SECTOR_FRHC_POS, MatrixValidator.N_HS));
        this.setSectorFRIN(getValue(mv, CacheStockSector.SECTOR_FRIN_POS, MatrixValidator.N_HS));
        this.setSectorFROGP(getValue(mv, CacheStockSector.SECTOR_FROGP_POS, MatrixValidator.N_HS));
        this.setSectorFRTEC(getValue(mv, CacheStockSector.SECTOR_FRTEC_POS, MatrixValidator.N_HS));
        this.setSectorFRTEL(getValue(mv, CacheStockSector.SECTOR_FRTEL_POS, MatrixValidator.N_HS));
        this.setSectorFRUT(getValue(mv, CacheStockSector.SECTOR_FRUT_POS, MatrixValidator.N_HS));


        return this;

    }

    private int getValue(MatrixValidator mv,int pos, int offset) {
        if (mv.getMatrix()[pos+offset][MatrixValidator.HS_COL] == 1)
            return mv.getMatrix()[pos+offset][MatrixValidator.HS_PERIOD_COL];
        else
            return 0;
    }



}
