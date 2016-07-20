package com.mltrading.models.util;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.influxdb.dto.BatchPoints;
import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.stock.StockHistory;

import java.io.*;

/**
 * Created by gmo on 07/07/2016.
 */
public class CsvFileReader {
    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";



    //CSV file header
    private static final String FILE_HEADER = "type,code,date,value,open,min,max,volume";

    private static final int TYPE_IDX = 0;
    private static final int CODE_IDX = 0;
    private static final int DATE_IDX = 0;
    private static final int VALUE_IDX = 0;
    private static final int OPEN_IDX = 0;
    private static final int MIN_IDX = 0;
    private static final int MAX_IDX = 0;
    private static final int VOLUME_IDX = 0;


    private FileWriter fileWriter = null;
    BufferedReader fileReader = null;
    BatchPoints bp = InfluxDaoConnector.getBatchPoints(HistoryParser.dbName);


    private void createHistory() {
        HistoryParser.saveHistory(bp, new StockHistory());
    }

    public void readData() {
        try {
            fileReader = new BufferedReader(new FileReader("ConsistencyExport.csv"));
            fileReader.readLine();
            String line = "";


            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(COMMA_DELIMITER);

                if (tokens.length > 0) {
                    String type = tokens[TYPE_IDX];

                }

            }

        } catch (FileNotFoundException e) {
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


}
