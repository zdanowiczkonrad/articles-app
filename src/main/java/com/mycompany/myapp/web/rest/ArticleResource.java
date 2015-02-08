package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Article;
import com.mycompany.myapp.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Article.
 */
@RestController
@RequestMapping("/api")
public class ArticleResource {

    private final Logger log = LoggerFactory.getLogger(ArticleResource.class);

    @Inject
    private ArticleRepository articleRepository;

    /**
     * POST  /articles -> Create a new article.
     */
    @RequestMapping(value = "/articles",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Article article) {
        log.debug("REST request to save Article : {}", article);
        articleRepository.save(article);
    }

    /**
     * GET  /articles -> get all the articles.
     */
    @RequestMapping(value = "/articles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Article> getAll() {
        log.debug("REST request to get all Articles");
        return articleRepository.findAll();
    }

    /**
     * GET  /articles/:id -> get the "id" article.
     */
    @RequestMapping(value = "/articles/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Article> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Article : {}", id);
        Article article = articleRepository.findOne(id);
        if (article == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    /**
     * DELETE  /articles/:id -> delete the "id" article.
     */
    @RequestMapping(value = "/articles/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        articleRepository.delete(id);
    }
}
