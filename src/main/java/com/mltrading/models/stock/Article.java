package com.mltrading.models.stock;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmo on 12/04/2016.
 */

/**
 * Document article form press
 */

@Document(collection = "JHI_ARTICLE")
public class Article implements Serializable{

    @Id
    ArticleKey key;

    String reference;

    List<String> lines;

    //List<String> topic;

    public Article() {
        lines = new ArrayList<String>();
        //topic = new ArrayList<String>();
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public ArticleKey getKey() {
        return key;
    }

    public void setKey(ArticleKey key) {
        this.key = key;
    }

    /*public List<String> getTopic() {
        return topic;
    }

    public void setTopic(List<String> topic) {
        this.topic = topic;
    }*/
}
