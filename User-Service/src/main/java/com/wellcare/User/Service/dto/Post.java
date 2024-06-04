package com.wellcare.User.Service.dto;

import com.wellcare.User.Service.Models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Post {
    private Long id;
    private LocalDateTime createdAt;
    private String location;
    private String content;
    private List<String> attachment = new ArrayList<>();
    private Integer noOfLikes = 0;
    private Integer noOfComments = 0;
    private User user;
    private Set<User> likes = new HashSet<>();

    private List<Comment> comments = new ArrayList<>();

    public Post() {
        this.createdAt = LocalDateTime.now();
        this.noOfLikes = 0;
    }

    public Post(String content) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.noOfLikes = 0;
        this.noOfComments = 0;
    }

    public Post(String location, List<String> attachment) {
        this.createdAt = LocalDateTime.now();
        this.location = location;
        this.attachment = new ArrayList<String>(attachment);
        this.noOfLikes = 0;
        this.noOfComments = 0;
    }


}
