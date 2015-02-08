package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Comment entity.
 */
public interface CommentRepository extends JpaRepository<Comment,Long>{

}
