package com.mltrading.models.parser.impl.investir;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.parser.HistoryCommon;
import com.mltrading.models.parser.HistoryIndiceParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.impl.investing.HistoryIndiceParserInvesting;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.StockIndice;
import com.mltrading.models.stock.cache.CacheStockHistory;
import com.mltrading.models.stock.cache.CacheStockIndice;
import org.influxdb.dto.BatchPoints;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HistoryIndiceParserInvestir extends ParserCommon implements HistoryIndiceParser,HistoryCommon {
    //https://investir.lesechos.fr/cours/historique-indice-cac-telecommunications,xpar,frtel,qs0011017769,isin.html
    static String refCode = "#tablehisto";
    HistoryIndiceParserInvesting indiceParser = new HistoryIndiceParserInvesting();
    //

    /**
     *
     * @param range
     */
    public void loaderFrom(int range) {

        for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {

            try {
                String url = g.getUrlInvestir();
                if (url.contains("investir"))
                    parser(url,g,range);

            }  catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            }

        }

        indiceParser.fetchCurrent(range);
    }


    /**
     *
     */
    public void loader() {


    }


    private void parser(String url, StockIndice g, int range) throws MalformedURLException, InterruptedException {
        String text = loadUrl(new URL(url));

        Document doc = Jsoup.parse(text);
        BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Elements links = doc.select(refCode).select("tr");
        int count = 0;
        for (Element link : links) {

            if (link.child(0).tag().getName().equals("th"))
                continue;

            if (count++ >= range)
                break;

            StockHistory stock = new StockHistory();
            stock.setCode(g.getCode());
            stock.setCodif(g.getCode());

            stock.setDayInvest(link.child(0).text());

            if (CacheStockHistory.CacheStockHistoryHolder().isInStockHistory(g.getCodif(), stock.getDay()))
                continue;

            stock.setValue(new Double(link.child(1).text().replaceAll(" ", "")
                .replace(",", ".").replace("-","0").replaceAll(String.valueOf((char) 160), "")));


            stock.setHighest(new Double(link.child(3).text().replaceAll(" ", "")
                .replace(",", ".").replace("-","0").replaceAll(String.valueOf((char) 160), "")));


            stock.setLowest(new Double(link.child(4).text().replaceAll(" ", "")
                .replace(",", ".").replace("-","0").replaceAll(String.valueOf((char) 160), "")));

            stock.setOpening(new Double(link.child(5).text().replaceAll(" ", "")
                .replace(",", ".").replace("-","0").replaceAll(String.valueOf((char) 160), "")));

            try {
                stock.setVolume(new Double(0));
            } catch (Exception e) {
                stock.setOpening(new Double(0));
            }
            stock.setConsensusNote(new Double(0));
            saveHistory(bp, stock);
            System.out.println(stock.toString());

        }
        InfluxDaoConnector.writePoints(bp);

    }

    @Override
    public void fetch() {

    }

    @Override
    public void fetchCurrent(int period) {
        loaderFrom(period);
    }
}
