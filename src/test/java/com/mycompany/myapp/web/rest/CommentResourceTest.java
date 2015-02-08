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
import java.util.List;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Comment;
import com.mycompany.myapp.repository.CommentRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CommentResource REST controller.
 *
 * @see CommentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CommentResourceTest {

    private static final String DEFAULT_AUTHOR = "SAMPLE_TEXT";
    private static final String UPDATED_AUTHOR = "UPDATED_TEXT";
    private static final String DEFAULT_MAIL = "SAMPLE_TEXT";
    private static final String UPDATED_MAIL = "UPDATED_TEXT";
    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE = new LocalDate();

    private static final Long DEFAULT_UPVOTES = 0L;
    private static final Long UPDATED_UPVOTES = 1L;

    @Inject
    private CommentRepository commentRepository;

    private MockMvc restCommentMockMvc;

    private Comment comment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommentResource commentResource = new CommentResource();
        ReflectionTestUtils.setField(commentResource, "commentRepository", commentRepository);
        this.restCommentMockMvc = MockMvcBuilders.standaloneSetup(commentResource).build();
    }

    @Before
    public void initTest() {
        comment = new Comment();
        comment.setAuthor(DEFAULT_AUTHOR);
        comment.setMail(DEFAULT_MAIL);
        comment.setTitle(DEFAULT_TITLE);
        comment.setContent(DEFAULT_CONTENT);
        comment.setDate(DEFAULT_DATE);
        comment.setUpvotes(DEFAULT_UPVOTES);
    }

    @Test
    @Transactional
    public void createComment() throws Exception {
        // Validate the database is empty
        assertThat(commentRepository.findAll()).hasSize(0);

        // Create the Comment
        restCommentMockMvc.perform(post("/api/comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comment)))
                .andExpect(status().isOk());

        // Validate the Comment in the database
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(1);
        Comment testComment = comments.iterator().next();
        assertThat(testComment.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testComment.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testComment.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testComment.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testComment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testComment.getUpvotes()).isEqualTo(DEFAULT_UPVOTES);
    }

    @Test
    @Transactional
    public void getAllComments() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the comments
        restCommentMockMvc.perform(get("/api/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(comment.getId().intValue()))
                .andExpect(jsonPath("$.[0].author").value(DEFAULT_AUTHOR.toString()))
                .andExpect(jsonPath("$.[0].mail").value(DEFAULT_MAIL.toString()))
                .andExpect(jsonPath("$.[0].title").value(DEFAULT_TITLE.toString()))
                .andExpect(jsonPath("$.[0].content").value(DEFAULT_CONTENT.toString()))
                .andExpect(jsonPath("$.[0].date").value(DEFAULT_DATE.toString()))
                .andExpect(jsonPath("$.[0].upvotes").value(DEFAULT_UPVOTES.intValue()));
    }

    @Test
    @Transactional
    public void getComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get the comment
        restCommentMockMvc.perform(get("/api/comments/{id}", comment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(comment.getId().intValue()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.upvotes").value(DEFAULT_UPVOTES.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingComment() throws Exception {
        // Get the comment
        restCommentMockMvc.perform(get("/api/comments/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Update the comment
        comment.setAuthor(UPDATED_AUTHOR);
        comment.setMail(UPDATED_MAIL);
        comment.setTitle(UPDATED_TITLE);
        comment.setContent(UPDATED_CONTENT);
        comment.setDate(UPDATED_DATE);
        comment.setUpvotes(UPDATED_UPVOTES);
        restCommentMockMvc.perform(post("/api/comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comment)))
                .andExpect(status().isOk());

        // Validate the Comment in the database
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(1);
        Comment testComment = comments.iterator().next();
        assertThat(testComment.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testComment.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testComment.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testComment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testComment.getUpvotes()).isEqualTo(UPDATED_UPVOTES);
    }

    @Test
    @Transactional
    public void deleteComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get the comment
        restCommentMockMvc.perform(delete("/api/comments/{id}", comment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(0);
    }
}
