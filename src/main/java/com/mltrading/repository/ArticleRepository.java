package com.mltrading.repository;

import com.mltrading.models.stock.Article;
import com.mltrading.models.stock.ArticleKey;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by gmo on 12/04/2016.
 */
public interface ArticleRepository extends MongoRepository<Article, ArticleKey> {

}
