package com.wellcare.User.Service.dto;

import com.wellcare.User.Service.Models.User;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Comment {
    private Long id;
    private String attachment;
    private User user;

    private long noOfLikes;
    private String content;
    private Set<User> commentLikes = new HashSet<>();
    private LocalDateTime createdAt;
    @AssertTrue(message = "Either attachment or content must be provided")
    public boolean isEitherAttachmentOrContentValid() {
        if (content == null || content.isBlank()) {
            return attachment == null || attachment.isBlank();
        } else {
            return true;
        }
    }

    public Comment() {
    }

    public Comment(User user, String content) {
        this.user = user;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.noOfLikes = 0;
    }

    public Comment(User user, String content, String attachment) {
        this.user = user;
        this.content = content;
        this.attachment = attachment;
        this.createdAt = LocalDateTime.now();
        this.noOfLikes = 0;
    }

}
