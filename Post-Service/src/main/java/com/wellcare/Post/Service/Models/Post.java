package com.wellcare.Post.Service.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JsonProperty("location")
    @Size(max = 255)
    private String location;

    @NotBlank
    @Size(max = 255)
    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "post_attachment", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "attachment_url")
    private List<String> attachment = new ArrayList<>();

    @NotNull
    @Min(0)
    private Integer noOfLikes = 0;

    @NotNull
    @Min(0)
    private Integer noOfComments = 0;

    private long  userid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<String> attachment) {
        this.attachment = attachment;
    }

    public Integer getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(Integer noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    public Integer getNoOfComments() {
        return noOfComments;
    }

    public void setNoOfComments(Integer noOfComments) {
        this.noOfComments = noOfComments;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

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
