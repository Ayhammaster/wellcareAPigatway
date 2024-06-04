package com.wellcare.Comment.Service.Controllers;

import com.wellcare.Comment.Service.Assemblers.CommentModelAssembler;

import com.wellcare.Comment.Service.Exceptions.CommentException;
import com.wellcare.Comment.Service.Exceptions.ResourceNotFoundException;
import com.wellcare.Comment.Service.Models.Comment;
import com.wellcare.Comment.Service.Models.CommentLike;
import com.wellcare.Comment.Service.Repositories.CommentRepository;
import com.wellcare.Comment.Service.Repositories.CommentlikeRepository;
import com.wellcare.Comment.Service.Storage.StorageService;
import com.wellcare.Comment.Service.payload.response.MessageResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentModelAssembler commentModelAssembler;




    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentlikeRepository commentlikeRepository;



    @Autowired
    StorageService storageService;

    @PostMapping("/{postId}")
    public ResponseEntity<EntityModel<Comment>> createComment(@Valid @ModelAttribute Comment comment,
                                                              @RequestParam(value = "file", required = false) MultipartFile file, @PathVariable Long postId,
                                                              HttpServletRequest request,@RequestHeader("userid") long userid) throws CommentException {

        try {



            comment.setUserid(userid);
            comment.setPostid(postId);

            comment.setCreatedAt(LocalDateTime.now());

            if (file != null && !file.isEmpty()) {
                System.out.println("Received file: " + file.getOriginalFilename());
                storageService.store(file);
                String filename = file.getOriginalFilename();
                String url = "http://localhost:8080/files/" + filename;
                comment.setAttachment(url);
            }

            commentRepository.save(comment);



            return ResponseEntity.ok(commentModelAssembler.toModel(comment));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();

        }
    }

    // to update a comment
    @PutMapping("/{commentId}")
    public ResponseEntity<EntityModel<Comment>> updateComment(@Valid @PathVariable Long commentId,
            @ModelAttribute Comment updatedComment, @RequestParam(value = "file", required = false) MultipartFile file)
            throws BadRequestException {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new ResourceNotFoundException("Comment", commentId);
        }

        Comment existingComment = optionalComment.get();

        existingComment.setContent(updatedComment.getContent());

        if (file != null && !file.isEmpty()) {
            System.out.println("Received file: " + file.getOriginalFilename());
            storageService.store(file);
            String filename = file.getOriginalFilename();
            String url = "http://localhost:8083/files/" + filename;
            existingComment.setAttachment(url);
        } else if (updatedComment.getAttachment() != null) {
            existingComment.setAttachment(updatedComment.getAttachment());

        }
        commentRepository.save(existingComment);
        return ResponseEntity.ok(commentModelAssembler.toModel(existingComment));
    }


    @Transactional
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, HttpServletRequest request )
            throws  CommentException {

        commentRepository.deleteById(commentId);

        return ResponseEntity.ok(new MessageResponse("Comment deleted successfully!"));
    }

    @Transactional
    @PutMapping("/like-switcher/{commentId}")
    public ResponseEntity<EntityModel<Comment>> toggleLikeComment(HttpServletRequest request,
            @PathVariable Long commentId,@RequestHeader("userid") long userId)
            throws  CommentException {

        try {


            // Retrieve the comment from the repository
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CommentException("Comment not found"));


            Optional<CommentLike> existingLike = commentlikeRepository.findByCommentIdAndUserId(commentId, userId);

            // Toggle like status
            if (existingLike.isPresent()) {
                // User already liked, remove the like
                commentlikeRepository.delete(existingLike.get());
                comment.setNoOfLikes(comment.getNoOfLikes() - 1);
            } else {
                // User didn't like, add a new like
                CommentLike newLike = new CommentLike();
                newLike.setComment(comment);
                newLike.setUserId(userId);
                commentlikeRepository.save(newLike);
                comment.setNoOfLikes(comment.getNoOfLikes() + 1);
            }

            // Save the updated comment
            Comment likedComment = commentRepository.save(comment);
            return ResponseEntity.ok(commentModelAssembler.toModel(likedComment));

        } catch (CommentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
