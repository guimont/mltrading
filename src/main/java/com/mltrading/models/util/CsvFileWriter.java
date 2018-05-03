package com.mltrading.models.util;


import com.mltrading.models.stock.StockHistory;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by gmo on 07/07/2016.
 */
public class CsvFileWriter {
    //Delimiter used in CSV file
    public static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";



    //CSV file header
    private static final String FILE_HEADER = "type,code,codeIsin,date";
    private static final String FILE_HEADER_EXPORT = "code,date,value,open,min,max,volume";
    private FileWriter fileWriter = null;

    public CsvFileWriter() {
        try {
            fileWriter = new FileWriter("ConsistencyExport.csv");
            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public CsvFileWriter(String fileName) {
        try {
            fileWriter = new FileWriter(fileName);
            //Write the CSV file header
            fileWriter.append(FILE_HEADER_EXPORT.toString());
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CsvFileWriter(String fileName, String appender) {
        try {
            fileWriter = new FileWriter(fileName);
            //Write the CSV file header
            fileWriter.append(appender);
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void writeData(StockHistory sh) {
        try {
            fileWriter.append(sh.getCode());
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(sh.getDay());
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(sh.getValue()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(sh.getOpening()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(sh.getLowest()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(sh.getHighest()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(sh.getVolume()));
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData(String type,String code, String codeIsin, String date, int period) {
        try {
            fileWriter.append(type);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(code);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(codeIsin);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(date);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(period));


            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void close() {
        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void writeData(String s) {
        try {
            fileWriter.append(s);
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
