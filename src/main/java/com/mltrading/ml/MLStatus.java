package com.mltrading.ml;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gmo on 16/02/2016.
 */
public class MLStatus implements Serializable{

    private double avgD1 = 0;
    private double avgD5 = 0;
    private double avgD20 = 0;

    private int errorRateD1 = 0;
    private int errorRateD5 = 0;
    private int errorRateD20 = 0;

    private List<MLPerformances> perfList;


    public void calculeAvgPrd() {
        double avgD1 = 0, avgD5 =0, avgD20 =0;
        int errD1 = 0, errD5 =0, errD20 =0;
        for (MLPerformances p : perfList) {
            if (p.getMlD1() != null) {
                avgD1 += p.getMlD1().getRealyield() - p.getMlD1().getYield();
                errD1 += p.getMlD1().isSign() == false ? 1 : 0;
            }
            if (p.getMlD5() != null) {
                avgD5 += p.getMlD5().getRealyield() - p.getMlD5().getYield();
                errD5 += p.getMlD5().isSign() == false ? 1 : 0;
            }
            if (p.getMlD20() != null) {
                avgD20 += p.getMlD20().getRealyield() - p.getMlD20().getYield();
                errD20 += p.getMlD20().isSign() == false ? 1 : 0;
            }
        }

        this.setAvgD1(avgD1 / 90 * 100);
        this.setAvgD5(avgD5 / 85 * 100);
        this.setAvgD20(avgD20/70*100);

        this.setErrorRateD1(errD1);
        this.setErrorRateD5(errD5);
        this.setErrorRateD20(errD20);

    }



    public double getAvgD1() {
        return avgD1;
    }

    public void setAvgD1(double avgD1) {
        this.avgD1 = avgD1;
    }

    public double getAvgD5() {
        return avgD5;
    }

    public void setAvgD5(double avgD5) {
        this.avgD5 = avgD5;
    }

    public double getAvgD20() {
        return avgD20;
    }

    public void setAvgD20(double avgD20) {
        this.avgD20 = avgD20;
    }

    public int getErrorRateD1() {
        return errorRateD1;
    }

    public void setErrorRateD1(int errorRateD1) {
        this.errorRateD1 = errorRateD1;
    }

    public int getErrorRateD5() {
        return errorRateD5;
    }

    public void setErrorRateD5(int errorRateD5) {
        this.errorRateD5 = errorRateD5;
    }

    public int getErrorRateD20() {
        return errorRateD20;
    }

    public void setErrorRateD20(int errorRateD6) {
        this.errorRateD20 = errorRateD6;
    }

    public List<MLPerformances> getPerfList() {
        return perfList;
    }

    public void replaceElementList(List<MLPerformances> rep, int col) throws Exception {
        for (int i = 0; i< this.getPerfList().size(); i++) {
            switch (col) {
                case 1 : this.getPerfList().get(i).setMlD1(rep.get(i).getMlD1());
                        break;
                case 5 : this.getPerfList().get(i).setMlD5(rep.get(i).getMlD5());
                    break;
                case 20 : this.getPerfList().get(i).setMlD20(rep.get(i).getMlD20());
                    break;
                default: throw new Exception("replaement imossible, perdio unknown");
            }
        }

    }

    public void setPerfList(List<MLPerformances> perfList) {
        this.perfList = perfList;
    }


}
