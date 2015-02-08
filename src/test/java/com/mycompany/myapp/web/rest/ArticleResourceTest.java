package com.mycompany.myapp.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Article;
import com.mycompany.myapp.repository.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ArticleResource REST controller.
 *
 * @see ArticleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ArticleResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_POSTED = new LocalDate(0L);
    private static final LocalDate UPDATED_POSTED = new LocalDate();

    private static final Long DEFAULT_VOTE_COUNT = 0L;
    private static final Long UPDATED_VOTE_COUNT = 1L;

    private static final BigDecimal DEFAULT_AVERAGE_VOTE = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_AVERAGE_VOTE = BigDecimal.ONE;
    private static final String DEFAULT_AUTHOR = "SAMPLE_TEXT";
    private static final String UPDATED_AUTHOR = "UPDATED_TEXT";

    @Inject
    private ArticleRepository articleRepository;

    private MockMvc restArticleMockMvc;

    private Article article;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArticleResource articleResource = new ArticleResource();
        ReflectionTestUtils.setField(articleResource, "articleRepository", articleRepository);
        this.restArticleMockMvc = MockMvcBuilders.standaloneSetup(articleResource).build();
    }

    @Before
    public void initTest() {
        article = new Article();
        article.setTitle(DEFAULT_TITLE);
        article.setContent(DEFAULT_CONTENT);
        article.setPosted(DEFAULT_POSTED);
        article.setVoteCount(DEFAULT_VOTE_COUNT);
        article.setAverageVote(DEFAULT_AVERAGE_VOTE);
        article.setAuthor(DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    public void createArticle() throws Exception {
        // Validate the database is empty
        assertThat(articleRepository.findAll()).hasSize(0);

        // Create the Article
        restArticleMockMvc.perform(post("/api/articles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(article)))
                .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articles = articleRepository.findAll();
        assertThat(articles).hasSize(1);
        Article testArticle = articles.iterator().next();
        assertThat(testArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArticle.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testArticle.getPosted()).isEqualTo(DEFAULT_POSTED);
        assertThat(testArticle.getVoteCount()).isEqualTo(DEFAULT_VOTE_COUNT);
        assertThat(testArticle.getAverageVote()).isEqualTo(DEFAULT_AVERAGE_VOTE);
        assertThat(testArticle.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllArticles() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articles
        restArticleMockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(article.getId().intValue()))
                .andExpect(jsonPath("$.[0].title").value(DEFAULT_TITLE.toString()))
                .andExpect(jsonPath("$.[0].content").value(DEFAULT_CONTENT.toString()))
                .andExpect(jsonPath("$.[0].posted").value(DEFAULT_POSTED.toString()))
                .andExpect(jsonPath("$.[0].voteCount").value(DEFAULT_VOTE_COUNT.intValue()))
                .andExpect(jsonPath("$.[0].averageVote").value(DEFAULT_AVERAGE_VOTE.intValue()))
                .andExpect(jsonPath("$.[0].author").value(DEFAULT_AUTHOR.toString()));
    }

    @Test
    @Transactional
    public void getArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.posted").value(DEFAULT_POSTED.toString()))
            .andExpect(jsonPath("$.voteCount").value(DEFAULT_VOTE_COUNT.intValue()))
            .andExpect(jsonPath("$.averageVote").value(DEFAULT_AVERAGE_VOTE.intValue()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArticle() throws Exception {
        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Update the article
        article.setTitle(UPDATED_TITLE);
        article.setContent(UPDATED_CONTENT);
        article.setPosted(UPDATED_POSTED);
        article.setVoteCount(UPDATED_VOTE_COUNT);
        article.setAverageVote(UPDATED_AVERAGE_VOTE);
        article.setAuthor(UPDATED_AUTHOR);
        restArticleMockMvc.perform(post("/api/articles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(article)))
                .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articles = articleRepository.findAll();
        assertThat(articles).hasSize(1);
        Article testArticle = articles.iterator().next();
        assertThat(testArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticle.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testArticle.getPosted()).isEqualTo(UPDATED_POSTED);
        assertThat(testArticle.getVoteCount()).isEqualTo(UPDATED_VOTE_COUNT);
        assertThat(testArticle.getAverageVote()).isEqualTo(UPDATED_AVERAGE_VOTE);
        assertThat(testArticle.getAuthor()).isEqualTo(UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void deleteArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc.perform(delete("/api/articles/{id}", article.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Article> articles = articleRepository.findAll();
        assertThat(articles).hasSize(0);
    }
}
