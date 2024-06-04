package com.wellcare.Post.Service.Repositories;

import com.wellcare.Post.Service.Models.ERole;
import com.wellcare.Post.Service.Models.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.userid = :userId order by p.createdAt desc")
    public Page<Post> findByUserId(@Param("userId") Long userId, Pageable pageable);




    @Query("SELECT COUNT(p) FROM Post p WHERE p.userid = :userId")
    Long countPostsByUserId(@Param("userId") Long userId);
}
