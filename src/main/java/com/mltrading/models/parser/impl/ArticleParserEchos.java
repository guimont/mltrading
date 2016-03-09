package com.mltrading.models.parser.impl;

import com.mltrading.models.parser.ArticleParser;

/**
 * Created by gmo on 09/03/2016.
 */
public class ArticleParserEchos implements ArticleParser {
    //http://investir.lesechos.fr/actions/actualites/action-accor,xpar,ac,fr0000120404,isin.html?page=5
    static String base = "http://investir.lesechos.fr/cours/profil-societe-action-";
    static String sep = ",";
    static String end = ",isin.html";

    static String refCode = "tbody";
}
