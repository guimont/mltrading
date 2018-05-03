package com.mltrading.models.parser.impl.investing;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.parser.HistoryCommon;
import com.mltrading.models.parser.HistoryIndiceParser;
import com.mltrading.models.parser.ParserCommon;
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

public class HistoryIndiceParserInvesting extends ParserCommon implements HistoryIndiceParser,HistoryCommon {
    //https://investir.lesechos.fr/cours/historique-indice-cac-telecommunications,xpar,frtel,qs0011017769,isin.html
    final String refCode = "tr";

    /**
     *
     * @param range
     */
    public void loaderFrom(int range) {

        for (StockIndice g : CacheStockIndice.getIndiceCache().values()) {

            try {
                String url = g.getUrlInvestir();
                if (url.contains("investing"))
                    parser(url,g,range);

            }  catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            }

        }
    }


    /**
     *
     */
    public void loader() {


    }


    private void parser(String url, StockIndice g, int range) throws MalformedURLException {
        String text = loadUrl(new URL(url));

        Document doc = Jsoup.parse(text);
        BatchPoints bp = InfluxDaoConnector.getBatchPoints(StockHistory.dbName);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Elements links = doc.select(refCode);

        doc.select("tr").get(7).child(0).hasAttr("data-real-value"); //true

        int count = 0;
        for (Element t : links) {
            if (t.child(0).hasAttr("data-real-value")) {
                try {
                    StockHistory indice = new StockHistory();
                    indice.setCode(g.getCode());
                    indice.setCodif(g.getCode());
                    indice.setDayInvest(t.children().get(0).text());

                    if (count++ >= range)
                        break;

                    if (CacheStockHistory.CacheStockHistoryHolder().isInStockHistory(g.getCode(), indice.getDay()))
                        continue;

                    if (checkCurentDay(indice.getDay()))
                        continue;

                    indice.setValue(new Double(t.children().get(1).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    indice.setOpening(new Double(t.children().get(2).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    indice.setHighest(new Double(t.children().get(3).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    indice.setLowest(new Double(t.children().get(4).text().replaceAll(" ", "").replace(".", "").replace(",", ".").replace("-", "0")));
                    System.out.println(indice.toString());
                    saveHistory(bp, indice);
                }
                catch (Exception e) {
                    System.out.println("url: " + url +" Failed");
                    continue;
                }


            }
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
