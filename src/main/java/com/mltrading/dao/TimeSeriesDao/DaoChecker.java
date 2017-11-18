package com.mltrading.dao.TimeSeriesDao;

import org.influxdb.dto.QueryResult;

public interface DaoChecker {


    /**
     *
     * @param list
     * @return false if list empty
     */
    default boolean checker(QueryResult list) {
        if (list == null || list.getResults() == null
            || list.getResults().get(0) == null
            || list.getResults().get(0).getSeries() == null
            || list.getResults().get(0).getSeries().get(0) == null)
            return false;

        return true;
    }
}
