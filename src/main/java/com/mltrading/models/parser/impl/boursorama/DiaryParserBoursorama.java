package com.mltrading.models.parser.impl.boursorama;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.parser.DiaryParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.StockDocument;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.cache.CacheStockGeneral;

import org.influxdb.dto.BatchPoints;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DiaryParserBoursorama extends ParserCommon implements DiaryParser {

    //http://www.boursorama.com/bourse/actions/agenda.phtml?symbole=1rPAI
    //http://www.boursorama.com/bourse/actions/agenda.phtml?symbole=FF11-SOLB

    private String SOURCE = "BOURSORAMA";

    private String startUrl = "http://www.boursorama.com/bourse/actions/agenda.phtml?symbole=1rP";

    private String startAUrl = "http://www.boursorama.com/bourse/actions/agenda.phtml?symbole=1rA";

    private String startSOLBUrl = "http://www.boursorama.com/bourse/actions/agenda.phtml?symbole=FF11-";

    private String refCode = "table";

    @Override
    public void fetch() {
        loader();
    }

    @Override
    public void fetchCurrent(int period) {
        DateTime dateTime = DateTime.now();
        dateTime = dateTime.minusDays(period);
        loaderFrom(dateTime);
    }


    /**
     * parse google indice page
     */
    public void loader() {
        loaderFrom(null);
    }


    /**
     * parse google indice page
     */
    public void loaderFrom(DateTime dateTime) {

        for (StockGeneral g : CacheStockGeneral.getIsinCache().values()) {
            try {

                String url = startUrl + g.getRealCodif();
                if (g.getRealCodif() == "SOLB")
                    url = startSOLBUrl + g.getRealCodif();
                if (g.getRealCodif() == "MT" || g.getRealCodif() == "UL")
                    url = startAUrl + g.getRealCodif();

                System.out.println(g.getRealCodif());
                parser(url, g, dateTime);

            }  catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            }

        }
    }




    private void parser(String url, StockGeneral g, DateTime dateTime) throws MalformedURLException, InterruptedException {

        String text = loadUrl(new URL(url));

        Document doc = Jsoup.parse(text);
        BatchPoints bp = InfluxDaoConnector.getBatchPointsV1(StockDocument.dbName);

        Elements links = doc.select(refCode);
        for (Element link : links) {
            Elements sublinks = link.children().select("tr");
            for (Element elt : sublinks) {
                Elements t = elt.select("td");

                /*check attribute condition*/
                if (t.size() != 3) continue;
                if (t.hasClass("tdv-author")) continue;



                if (t.get(1).text().equalsIgnoreCase("date")) continue;

                StockDocument d = new StockDocument();
                d.setCode(g.getRealCodif());
                d.setDayInvest(t.get(1).text());
                d.setRef(t.get(0).text());
                d.setSource(SOURCE);

                //if (dateTime!= null  && dateTime.isBefore(d.getTimeInsert())) return;
                saveDiary(bp, d);
                System.out.println(d.toString());
            }
        }
        InfluxDaoConnector.writePoints(bp);
    }


}
