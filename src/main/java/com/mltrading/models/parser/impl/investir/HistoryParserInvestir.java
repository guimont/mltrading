package com.mltrading.models.parser.impl.investir;

import com.mltrading.dao.InfluxDaoConnector;
import com.mltrading.models.parser.HistoryCommon;
import com.mltrading.models.parser.HistoryParser;
import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.models.stock.StockHistory;
import com.mltrading.models.stock.cache.CacheStockGeneral;

import com.mltrading.models.stock.cache.CacheStockHistory;
import org.influxdb.dto.BatchPoints;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HistoryParserInvestir extends ParserCommon implements HistoryParser,HistoryCommon {
    //https://investir.lesechos.fr/cours/action-arcelormittal-sa,xams,mt,lu1598757687,isin.html
    static String startUrl = "https://investir.lesechos.fr/cours/historique-action-";
    static String endUrl = ",isin.html";
    static String refCode = "#tablehisto";
    static String separator = ",";

    /**
     *
     * @param range
     */
    public void loaderFrom(int range) {

        for (StockGeneral g : CacheStockGeneral.getIsinCache().values()) {
            String url = startUrl + g.getName().toLowerCase().replaceAll(" ", "-")
                + separator + g.getPlace()  + separator + g.getRealCodif().toLowerCase()+separator+g.getCode().toLowerCase()+endUrl;
            try {
                parser(url,g,range);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *
     */
    public void loader() {


    }


    private void parser(String url, StockGeneral g, int range) throws MalformedURLException {

        try {
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

                StockHistory stock = new StockHistory(g);
                stock.setDayInvest(link.child(0).text());

                if (CacheStockHistory.CacheStockHistoryHolder().isInStockHistory(g.getCodif(), stock.getDay()))
                    continue;


                stock.setValue(new Double(link.child(1).text().replaceAll(" ", "")
                    .replace(",", ".").replace("-", "0").replaceAll(String.valueOf((char) 160), "")));


                stock.setHighest(new Double(link.child(3).text().replaceAll(" ", "")
                    .replace(",", ".").replace("-", "0").replaceAll(String.valueOf((char) 160), "")));


                stock.setLowest(new Double(link.child(4).text().replaceAll(" ", "")
                    .replace(",", ".").replace("-", "0").replaceAll(String.valueOf((char) 160), "")));

                stock.setOpening(new Double(link.child(5).text().replaceAll(" ", "")
                    .replace(",", ".").replace("-", "0").replaceAll(String.valueOf((char) 160), "")));

                stock.setVolume(new Double(link.child(6).text().replaceAll(" ", "")
                    .replace(",", ".").replace("-", "0").replaceAll(String.valueOf((char) 160), "")));


                stock.setConsensusNote(new Double(0));
                saveHistory(bp, stock);
                System.out.println(stock.toString());

            }
            InfluxDaoConnector.writePoints(bp);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public void fetch() {

    }

    @Override
    public void fetchSpecific(StockGeneral g) {
        String url = startUrl + g.getName().toLowerCase().replaceAll(" ", "-")
            + separator + g.getPlace()  + separator + g.getRealCodif().toLowerCase()+separator+g.getCode().toLowerCase()+endUrl;
        try {
            parser(url,g,20);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fetchCurrent(int period) {
        loaderFrom(period);
    }
}
