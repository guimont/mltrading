package com.mltrading.models.parser.impl;


import com.mltrading.models.parser.ParserCommon;
import com.mltrading.models.parser.StockParser;
import com.mltrading.models.stock.CacheStockGeneral;
import com.mltrading.models.stock.Stock;
import com.mltrading.models.stock.StockGeneral;
import com.mltrading.repository.StockRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 05/01/2016.
 */
public class StockParserInvestir  implements StockParser{

    static String base = "http://investir.lesechos.fr/cours/profil-societe-action-";
    static String sep = ",";
    static String end = ",isin.html";

    static String refCode = "tbody";

    //http://investir.lesechos.fr/cours/profil-societe-action-accor,xpar,ac,fr0000120404,isin.html

    @Override
    public void fetch(StockRepository repository) {
        loader(repository);
    }


    private void loader(StockRepository repository) {
        for (StockGeneral g: CacheStockGeneral.getIsinCache().values()) {

            String url = base + CacheStockGeneral.getIsinCache().get(g.getCode()).getName().toLowerCase().replaceAll(" ","-") + sep + g.getPlace().toLowerCase() + sep  + g.getCodif().toLowerCase() + sep + g.getCode().toLowerCase() +end;


            try {
                String text;

                System.out.println(url);
                text = ParserCommon.loadUrl(new URL(url));

                if (g.getCode().toLowerCase().equals("fr0000124711")) {
                    String test ="";
                }

                if (text != null) {
                    Document doc = Jsoup.parse(text);

                    String sector=g.getSector();

                    String marche = doc.select("div.bloc-tabs2").get(0).getAllElements().get(3).text();
                    String indice = doc.select("div.bloc-tabs2").get(0).getAllElements().get(6).text();
                    String codeif = doc.select("div.bloc-tabs2").get(0).getAllElements().get(9).text();
                    String code = doc.select("div.bloc-tabs2").get(0).getAllElements().get(12).text();
                    String bloomberg = doc.select("div.bloc-tabs2").get(0).getAllElements().get(15).text();
                    String reuters = doc.select("div.bloc-tabs2").get(0).getAllElements().get(18).text();
                    String eligib = doc.select("div.bloc-tabs2").get(0).getAllElements().get(21).text();
                    String titleNb = doc.select("div.bloc-tabs2").get(0).getAllElements().get(24).text();
                    String capitalization = doc.select("div.bloc-tabs2").get(0).getAllElements().get(27).text();
                    String ownFounds = doc.select("div.bloc-tabs2").get(0).getAllElements().get(30).text();
                    String debt = doc.select("div.bloc-tabs2").get(0).getAllElements().get(33).text();
                    String netDebt="";
                    if (doc.select("div.bloc-tabs2").get(0).getAllElements().size() > 35) {
                        netDebt = doc.select("div.bloc-tabs2").get(0).getAllElements().get(36).text();
                    }

                    Elements dir = doc.select(".effectifs").toggleClass("flex-viewport").select("li");
                    List<String> peoples = new ArrayList<String>();
                    for (Element p:dir) {
                        String personn = p.text();
                        peoples.add(personn);
                    }

                    Stock stock = new Stock(marche, indice, codeif, code, bloomberg, reuters, eligib, titleNb, capitalization, ownFounds, debt, netDebt, sector);

                    repository.save(stock);
                    Thread.sleep(500);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR for : " + g.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
