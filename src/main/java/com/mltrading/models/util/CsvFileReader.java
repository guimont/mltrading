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

    private static final int CODE_IDX = 0;
    private static final int DATE_IDX =1;
    private static final int VALUE_IDX = 2;
    private static final int MIN_IDX = 4;
    private static final int MAX_IDX = 3;
    private static final int OPEN_IDX = 5;

    private static final int VOLUME_IDX = 0;


    private FileWriter fileWriter = null;
    BufferedReader fileReader = null;



    public void readData() {
        try {

            BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

            fileReader = new BufferedReader(new FileReader("ConsistencyImport.csv"));
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

                    StockHistory sh = new StockHistory();
                    sh.setCode(code);
                    sh.setCodif(code);
                    sh.setDayInvest(date);
                    sh.setValue(new Double(value));
                    sh.setLowest(new Double(min));
                    sh.setHighest(new Double(max));
                    sh.setOpening(new Double(open));

                    HistoryParser.saveHistory(bp, sh);

                }

            }


            InfluxDaoConnector.writePoints(bp);

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
