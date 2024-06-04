package com.wellcare.Comment.Service.Repositories;

import com.wellcare.Comment.Service.Models.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

        @Query("SELECT c FROM Comment c WHERE c.postid = :postId")
        List<Comment> findAllByPost(long postId);
      

}
