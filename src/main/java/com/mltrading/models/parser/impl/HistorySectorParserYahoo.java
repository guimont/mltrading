package com.mltrading.models.parser.impl;

import com.google.inject.Singleton;

import com.mltrading.models.parser.HistorySectorParser;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by gmo on 23/11/2015.
 */

@Singleton
@Deprecated
public class HistorySectorParserYahoo implements HistorySectorParser {
    @Override
    public void fetch() {
        throw new NotImplementedException();//loader();
    }

    @Override
    public void fetchCurrent(int period) {
       throw  new NotImplementedException();
    }



    //https://fr.finance.yahoo.com/q/hp?s=FRCM.PA&b=2&a=00&c=2015&e=2&d=00&f=2015&g=d

    static String startUrl="https://fr.finance.yahoo.com/q/hp?s=";
    static String endUrl ="&a=00&b=3&c=2010&g=d&z=66&y=";
    static int PAGINATION = 66;
    static String refCode = "tbody";
    static int MAXPAGE = 1518;


    /*throw new NotImplementedException();
    public void loaderSpecific(String date) {
        String endUrl = "&b=2&a=00&c=2015&e=2&d=00&f=2015&g=d";

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

                try {
                    String text;
                    String url = startUrl + g.getCode() + "." + g.getPlace() + endUrl ;

                    text = ParserCommon.loadUrl(new URL(url));

                    Document doc = Jsoup.parse(text);
                    BatchPoints bp = InfluxDaoConnector.getBatchPoints();


                    Elements links = doc.select(refCode);


                    StockSector sect = new StockSector(g.getCode(), g.getName(), g.getPlace());
                    sect.setDayYahoo( links.get(14).child(1).child(0).text());
                    sect.setOpening(new Double( links.get(14).child(1).child(1).text().replaceAll(" ", "").replace(",", ".")));
                    sect.setHighest(new Double( links.get(14).child(1).child(2).text().replaceAll(" ", "").replace(",", ".")));
                    sect.setLowest(new Double( links.get(14).child(1).child(3).text().replaceAll(" ", "").replace(",", ".")));
                    sect.setValue(new Double( links.get(14).child(1).child(4).text().replaceAll(" ", "").replace(",", ".")));
                    HistorySectorParser.saveHistory(bp, sect);
                    System.out.println(sect.toString());


                    InfluxDaoConnector.writePoints(bp);


                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR for : " + g.getCode());
                }
            }

    }


    public void loader() {

        for (StockSector g : CacheStockSector.getSectorCache().values()) {

            for (int numPage = 0; numPage <= MAXPAGE; numPage += PAGINATION) {
                try {
                    String text;
                    String url = startUrl + g.getCode() + "." + g.getPlace() + endUrl + numPage;

                    text = ParserCommon.loadUrl(new URL(url));

                    Document doc = Jsoup.parse(text);
                    BatchPoints bp = InfluxDaoConnector.getBatchPoints();


                    Elements links = doc.select(refCode);
                    for (Element link : links) {

                        if (link.children().size() > 40) {
                            Elements sublinks = link.children().select("tr");
                            for (Element elt : sublinks) {
                                Elements t = elt.select("td");
                                if (t.size() > 3) {

                                    StockSector sect = new StockSector(g.getCode(), g.getName(), g.getPlace());
                                    sect.setDayYahoo(t.get(0).text());
                                    sect.setOpening(new Double(t.get(1).text().replaceAll(" ", "").replace(",", ".")));
                                    sect.setHighest(new Double(t.get(2).text().replaceAll(" ", "").replace(",", ".")));
                                    sect.setLowest(new Double(t.get(3).text().replaceAll(" ", "").replace(",", ".")));
                                    sect.setValue(new Double(t.get(4).text().replaceAll(" ", "").replace(",", ".")));
                                    HistorySectorParser.saveHistory(bp, sect);
                                    System.out.println(sect.toString());
                                }
                            }
                        }
                    }
                    InfluxDaoConnector.writePoints(bp);


                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR for : " + g.getCode());
                }
            }

        }
    }*/
}
