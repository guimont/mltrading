package com.mltrading.model;

import com.mltrading.config.MLProperties;
import com.mltrading.models.stock.cache.CacheStockHistory;

import com.mltrading.models.stock.StockHistory;
import org.junit.Test;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by gmo on 08/03/2017.
 */
public class CacheStockHistoryTest {

    @Test
    public void testCache() {
        MLProperties.load();
        CacheStockHistory cache = CacheStockHistory.CacheStockHistoryHolder();

        List<String> rangeDate = cache.getDateHistoryListOffsetLimit("ORA",1500);
        assertThat(rangeDate.size()).isEqualTo(1500);



        long now = System.currentTimeMillis();
        String dateCurrent = null;
        for (String date: rangeDate) {
            StockHistory sh = cache.getStockHistory("ORA", date, true);
            assertThat(sh != null).isTrue();
            assertThat(sh.getDay().equals(date)).isTrue();
            if (dateCurrent != null) assertThat(dateCurrent.equals(sh.getDay())).isTrue();

            StockHistory shAfter = cache.getStockHistoryDayAfter("ORA", date, true);
            if (shAfter!= null)  dateCurrent = shAfter.getDay();

        }
        System.out.print("Time to process test: ");
        System.out.print( System.currentTimeMillis() - now);

    }

    @Test
    public void testCacheOffset() {
        /*MLProperties.load();
        CacheStockHistory cache = CacheStockHistory.CacheStockHistoryHolder();

        List<String> rangeDate = cache.getDateHistoryList("ORA", 1500);
        assertThat(rangeDate.size()).isEqualTo(1500);

*/




    }

    @Test
    public void testCacheSimple() {
        MLProperties.load();
        CacheStockHistory cache = CacheStockHistory.CacheStockHistoryHolder();

        String date = cache.getLastDateHistory("ORA");
        assertThat(date).isNotNull();


        StockHistory sh = cache.getStockHistory("AC", "2014-11-12");
        assertThat(sh).isNotNull();

    }


    @Test
    public void testCacheLast() {
        MLProperties.load();
        CacheStockHistory cache = CacheStockHistory.CacheStockHistoryHolder();

        List<StockHistory> list = cache.getStockHistoryLast("ORA",20);
        assertThat(list.size()).isEqualTo(20);

    }



    @Test
    public void testCacheDateInvert() {
        MLProperties.load();
        CacheStockHistory cache = CacheStockHistory.CacheStockHistoryHolder();

        List<StockHistory> list = cache.getStockHistoryDateInvert("ORA", "2014-11-12", 5, true);
        assertThat(list.size()).isEqualTo(5);

        List<StockHistory> list2 = cache.getStockHistoryDateInvert("ORA", "2014-11-12", 5, true);
        assertThat(list2.size()).isEqualTo(5);


        assertThat(list.get(0).getDay()).isEqualTo(list2.get(0).getDay());

    }


}
