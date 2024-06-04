package com.wellcare.Post.Service.Controllers;


import com.wellcare.Post.Service.Assemblers.PostModelAssembler;
import com.wellcare.Post.Service.Exceptions.PostException;
import com.wellcare.Post.Service.Exceptions.ResourceNotFoundException;
import com.wellcare.Post.Service.Exceptions.UserException;
import com.wellcare.Post.Service.Models.ERole;
import com.wellcare.Post.Service.Models.Post;
import com.wellcare.Post.Service.Repositories.PostRepository;
import com.wellcare.Post.Service.Storage.StorageService;
import com.wellcare.Post.Service.payload.response.MessageResponse;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    PostRepository postRepository;


    @Autowired
    PostModelAssembler postModelAssembler;


    @Autowired
    StorageService storageService;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PagedResourcesAssembler<Post> pagedResourcesAssembler;

    @Autowired
    private EntityManager entityManager;

   

@Transactional
@PostMapping("/new-post")
public ResponseEntity<EntityModel<Post>> createPost(HttpServletRequest request,
        @Valid @ModelAttribute Post post,
        @RequestParam(value = "file", required = false) MultipartFile[] files,
        @RequestHeader("userid") long userid) throws UserException {
    try {
        

        post.setUserid(userid);
        post.setCreatedAt(LocalDateTime.now());

        List<String> attachmentUrls = new ArrayList<>();
      
        
        if (files != null) {
            for (MultipartFile file : files) {
                System.out.println("Received file: " + file.getOriginalFilename());
                String fileName = storageService.store(file); 
                String url = "http://localhost:8082/files/" + fileName;
                attachmentUrls.add(url);
            }
        }

      
        post.setAttachment(attachmentUrls); // Set attachments to the post

        Post createdPost = postRepository.save(post);

        EntityModel<Post> postModel = postModelAssembler.toModel(createdPost);

        // Pass the userId to the linkTo method
        return new ResponseEntity<>(postModel, HttpStatus.CREATED);
    } catch (Exception ex) {
        // Log the complete exception details for better analysis
        logger.error("Error processing JWT token", ex);
        if (ex instanceof JwtException) {
            throw new UserException("Invalid JWT token: " + ex.getMessage());
        } else {
            throw new UserException("Error processing JWT token: " + ex.getMessage());
        }
    }
}


@Transactional
@PutMapping("/{postId}")
public ResponseEntity<EntityModel<Post>> updatePost(HttpServletRequest request,
        @ModelAttribute Post updatedPost,
        @PathVariable Long postId,
        @RequestParam(value = "file", required = false) MultipartFile[] files,
                                                    @RequestHeader("userid") long userid) throws PostException, UserException {
    try {


        Optional<Post> existingPostOptional = postRepository.findById(postId);
        if (existingPostOptional.isEmpty()) {
            throw new ResourceNotFoundException("Post", postId);
        }
        Post existingPost = existingPostOptional.get();

        if (existingPost.getUserid() != userid ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        entityManager.detach(existingPost);

        // Update only the fields that are not null in the updatedPost
        if (updatedPost.getContent() != null) {
            existingPost.setContent(updatedPost.getContent());
        }
        if (updatedPost.getLocation() != null) {
            existingPost.setLocation(updatedPost.getLocation());
        }

        // Keep existing attachments if files are not updated
        List<String> existingAttachments = existingPost.getAttachment() != null ? existingPost.getAttachment() : new ArrayList<>();

        List<String> attachmentUrls = new ArrayList<>(existingAttachments);

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                storageService.store(file);
                String filename = file.getOriginalFilename();
                String url = "http://localhost:8082/files/" + filename;
                attachmentUrls.add(url);
            }
        }

        existingPost.setAttachment(attachmentUrls); // Set attachments to the existing post

        Post savedPost = postRepository.save(existingPost);

        EntityModel<Post> postModel = postModelAssembler.toModel(savedPost);
        return new ResponseEntity<>(postModel, HttpStatus.OK);
    } catch (Exception ex) {
        logger.error("Error updating post", ex);
        throw new PostException("Error updating post: " + ex.getMessage());
    }
}


    @GetMapping("/{userId}")
    public ResponseEntity<PagedModel<EntityModel<Post>>> getPostsByUserId(@PathVariable Long userId,
            Pageable pageable) {
        Page<Post> userPosts = postRepository.findByUserId(userId, pageable);

        PagedModel<EntityModel<Post>> pagedModel = pagedResourcesAssembler.toModel(userPosts, postModelAssembler);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);

    }

    @GetMapping("/{userId}/count")
    public ResponseEntity<Long> getPostCountByUserId(@PathVariable Long userId) {
        Long postCount = postRepository.countPostsByUserId(userId);
        return new ResponseEntity<>(postCount, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{postId}")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable Long postId) {
        try {
            Optional<Post> optionalPost = postRepository.findById(postId);
            if (optionalPost.isEmpty()) {
                throw new ResourceNotFoundException("Post", postId);
            }
            Post post = optionalPost.get();
            

            
            postRepository.delete(post);
            return ResponseEntity.ok(new MessageResponse("Post deleted successfully"));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Failed to delete post: Post with ID " + postId + " not found"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to delete post with ID: " + postId));
        }
    }





}