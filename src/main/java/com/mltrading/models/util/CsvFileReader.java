package com.mltrading.models.util;

import com.mltrading.dao.InfluxDaoConnector;

import com.mltrading.ml.MatrixValidator;
import com.mltrading.models.parser.Analyse;
import com.mltrading.models.parser.HistoryCommon;
import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.stock.StockAnalyse;
import com.mltrading.models.stock.StockHistory;
import org.influxdb.dto.BatchPoints;

import java.io.*;
import java.util.HashMap;

/**
 * Created by gmo on 07/07/2016.
 */
public class CsvFileReader implements HistoryCommon{
    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";



    //CSV file header
    private static final String FILE_HEADER = "type,code,date,value,open,min,max,volume";

    private static final int CODE_IDX = 0;
    private static final int DATE_IDX =1;
    private static final int VALUE_IDX = 2;
    private static final int MIN_IDX = 4;
    private static final int MAX_IDX = 3;
    private static final int OPEN_IDX = 5;
    private static final int VOLUME_IDX = 6;




    BufferedReader fileReader = null;

    public CsvFileReader(String fileName, boolean isAT) {
        System.out.println("############################################################");
        System.out.println("Import start !!!");
        System.out.println("############################################################");

        if (isAT)
            readDataAT(fileName);
        else
            readData(fileName);
    }

    public CsvFileReader() {
        readData("ConsistencyImport.csv");
    }


    public CsvFileReader(String fileName,  HashMap<String, MatrixValidator> mapMV ) {
        readData(fileName, mapMV );
    }

    private void readData(String fileName,  HashMap<String, MatrixValidator> mapMV ) {
        try {

            fileReader = new BufferedReader(new FileReader(fileName));
            fileReader.readLine();
            String line = "";
            while ((line = fileReader.readLine()) != null) {
                MatrixValidator mv = new MatrixValidator();
                String[] tokens = line.split(COMMA_DELIMITER);
                mapMV.put(tokens[0],mv.importMatrixValidator(tokens));
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }

        }

    }


    private void readData(String fileName) {
        try {

            BatchPoints bp = InfluxDaoConnector.getBatchPointsV1(StockHistory.dbName);

            fileReader = new BufferedReader(new FileReader(fileName));
            fileReader.readLine();
            String line = "";


            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(COMMA_DELIMITER);

                if (tokens.length > 0) {
                    String code = tokens[CODE_IDX];
                    String date = tokens[DATE_IDX];
                    String value = tokens[VALUE_IDX];
                    String min = tokens[MIN_IDX];
                    String max = tokens[MAX_IDX];
                    String open = tokens[OPEN_IDX];
                    String volume = tokens[VOLUME_IDX];

                    StockHistory sh = new StockHistory();
                    sh.setCode(code);
                    sh.setCodif(code);
                    sh.setDayImport(date);
                    sh.setValue(new Double(value));
                    sh.setLowest(new Double(min));
                    sh.setHighest(new Double(max));
                    sh.setOpening(new Double(open));
                    sh.setVolume(new Double(volume));

                    saveHistory(bp, sh);


                }

            }
            InfluxDaoConnector.writePoints(bp);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("############################################################");
                System.out.println("Import is end  !!!");
                System.out.println("############################################################");
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }

        }
    }


    private static final int MME12 = 2;
    private static final int MME26 = 3;
    private static final int MACD = 4;
    private static final int MMA20 = 5;
    private static final int MMA50 = 6;
    private static final int MOMENTUM = 7;
    private static final int STDDEV = 8;
    private static final int GARCH20 = 9;
    private static final int GARCH50 = 10;
    private static final int GARCH100 = 11;
    private static final int GARCHVOL20 = 12;
    private static final int GARCHVOL50 = 13;
    private static final int GARCHVOL100 = 14;

    private void readDataAT(String fileName) {
        try {

            fileReader = new BufferedReader(new FileReader(fileName));
            fileReader.readLine();
            String line = "";





            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(COMMA_DELIMITER);

                if (tokens.length > 0) {
                    String code = tokens[CODE_IDX];
                    String date = tokens[DATE_IDX];
                    String mme12 = tokens[MME12];
                    String mme26 = tokens[MME26];
                    String macd = tokens[MACD];
                    String mma20 = tokens[MMA20];
                    String mma50 = tokens[MMA50];
                    String momentum = tokens[MOMENTUM];
                    String stddev = tokens[STDDEV];
                    String garch20 = tokens[GARCH20];
                    String garch50 = tokens[GARCH50];
                    String garch100 = tokens[GARCH100];
                    String garchvol20 = tokens[GARCHVOL20];
                    String garchvol50 = tokens[GARCHVOL50];
                    String garchvol100 = tokens[GARCHVOL100];



                    StockAnalyse sa = new StockAnalyse();
                    sa.setDay(date);
                    sa.setMme12(new Double(mme12));
                    sa.setMme26(new Double(mme26));
                    sa.setMacd(new Double(macd));
                    sa.setMma20(new Double(mma20));
                    sa.setMma50(new Double(mma50));
                    sa.setMomentum(new Double(momentum));
                    sa.setStdDev(new Double(stddev));
                    sa.setGarch20(new Double(garch20));
                    sa.setGarch50(new Double(garch50));
                    sa.setGarch100(new Double(garch100));
                    sa.setGarch_vol_20(new Double(garchvol20));
                    sa.setGarch_vol_50(new Double(garchvol50));
                    sa.setGarch_vol_100(new Double(garchvol100));

                    Analyse.saveAnalysis(code, sa);

                }
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("############################################################");
                System.out.println("Import is end  !!!");
                System.out.println("############################################################");
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }

        }
    }




}
