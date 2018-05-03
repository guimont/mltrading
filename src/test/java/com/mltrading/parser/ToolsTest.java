package com.mltrading.parser;

import com.mltrading.config.MLProperties;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ToolsTest {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";


    //CSV file header
    private static final String FILE_HEADER = "type,code,date,value,open,min,max,volume";

    private static final int CODE_IDX = 0;
    private static final int DATE_IDX = 0;
    private static final int VALUE_IDX = 1;
    private static final int OPEN_IDX = 2;
    private static final int MAX_IDX = 3;
    private static final int MIN_IDX = 4;
    private static final int VOLUME_IDX = 5;

    @Test
    public void testTools() throws IOException {
        MLProperties.load();

        BufferedReader fileReader = null;

        fileReader = new BufferedReader(new FileReader("airbus_extract.csv"));
        fileReader.readLine();
        String line = "";

        List<String> list = new ArrayList<>();



        while ((line = fileReader.readLine()) != null) {
            String[] tokens = line.split("\t");

            if (tokens.length > 0) {
                String code = "MT";
                String date = tokens[DATE_IDX];
                DateTime t =setDayInvest(date);
                String value = tokens[VALUE_IDX].replaceAll(",",".");
                String open = tokens[OPEN_IDX].replaceAll(",",".");
                String min = tokens[MIN_IDX].replaceAll(",",".");
                String max = tokens[MAX_IDX].replaceAll(",",".");
                double cur = 1;

                if (tokens[VOLUME_IDX].contains("K"))
                    cur = 1000;
                else if (tokens[VOLUME_IDX].contains("M"))
                    cur = 1000000;
                String volume = tokens[VOLUME_IDX].replaceAll(" ","").replaceAll(",",".")
                    .replace("M","").replace("K","");

                Double vol = Math.ceil(Double.parseDouble(volume) * cur);

                list.add(code+COMMA_DELIMITER+t.toString().substring(0,19).replace("T00","T09")+"Z"
                    +COMMA_DELIMITER+value+COMMA_DELIMITER+open+COMMA_DELIMITER+min+COMMA_DELIMITER+max+COMMA_DELIMITER+vol);

            }
        }

        for (int i = list.size() -1;i > -1; i--) {
            System.out.println(list.get(i));
        }
    }

    @Test
    public void duplicateTest() throws IOException {
        MLProperties.load();
        duplicate("stock.csv","stock_s.csv");
        duplicate("sector.csv","sector_s.csv");
        duplicate("raw.csv","raw_s.csv");
        duplicate("indice.csv","indice_s.csv");
        duplicate("vcac.csv","vcac_s.csv");
    }


    private void duplicate(String name, String output) throws IOException {


        BufferedReader fileReader = null;
        FileWriter fileWriter = null;

        fileReader = new BufferedReader(new FileReader(name));
        fileWriter = new FileWriter(output);
        fileReader.readLine();
        String line ;





        String date = "";
        while ((line = fileReader.readLine()) != null) {
            String[] tokens = line.split(COMMA_DELIMITER);
            if (date.equals(tokens[1]))
                continue;
            fileWriter.write(line);
            fileWriter.write("\n");
            date = tokens[1];
        }

        fileReader.close();
        fileWriter.flush();
        fileWriter.close();

    }

    public DateTime setDayInvest(String day) {

        String DD = day.substring(0, 2);
        String MM = day.substring(3,5);
        String YY = day.substring(6,10);
        DateTime timeInsert = new DateTime( YY + "-" + MM + "-" + DD);
        return timeInsert;
    }
}
