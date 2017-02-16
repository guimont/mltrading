package com.mltrading.dao.mongoFile;

/**
 * Created by gmo on 14/02/2017.
 */
public class QueryMongoRequest {
    private String path;

    public QueryMongoRequest(String s) {
        path = s;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
