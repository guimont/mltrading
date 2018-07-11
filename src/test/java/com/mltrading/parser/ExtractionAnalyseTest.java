package com.mltrading.parser;

import com.mltrading.config.MLProperties;
import com.mltrading.models.parser.Analyse;
import org.junit.Test;

/**
 * Created by gmo on 17/03/2017.
 */
public class ExtractionAnalyseTest {

    @Test
    public void testExtractionDaily() {
        MLProperties.load();
        int period= 20;
        Analyse a = new Analyse();
        try {
            a.processDaily(period);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
