package com.mltrading.web.rest;

import com.mltrading.repository.ArticleRepository;
import com.mltrading.service.ExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gmo on 12/04/2016.
 */


@RestController
@RequestMapping("/api")
public class ArticleResource {

    private static final Logger log = LoggerFactory.getLogger(ArticleResource.class);

    private static ExtractionService service = new ExtractionService();

    @javax.inject.Inject
    private ArticleRepository articleRepository;

    @RequestMapping(value = "/extractionArticles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionArticles() {
        service.extractArticlesFull();
        return "ok";
    }


    @RequestMapping(value = "/extractionArticle",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionArticle() {
        service.extractArticleFull(articleRepository);
        return "ok";
    }



    @RequestMapping(value = "/extractionNotation",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExtractionNotation() {
        service.extractNotationFull(articleRepository);
        return "ok";
    }



}
