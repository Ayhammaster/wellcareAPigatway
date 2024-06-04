package com.wellcare.Comment.Service.Repositories;

import com.wellcare.Comment.Service.Models.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface CommentlikeRepository  extends JpaRepository<CommentLike, Long> {
    @Query("SELECT cl FROM CommentLike cl WHERE cl.comment.id = :commentId AND cl.userId = :userId")
    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);

}
