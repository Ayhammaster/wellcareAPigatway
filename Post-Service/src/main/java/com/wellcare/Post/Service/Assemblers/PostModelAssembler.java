package com.wellcare.Post.Service.Assemblers;

import com.wellcare.Post.Service.Controllers.PostController;
import com.wellcare.Post.Service.Exceptions.PostException;
import com.wellcare.Post.Service.Models.Post;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {
    
    @Override
    public EntityModel<Post> toModel(Post post) {

        return EntityModel.of(
                post,
                linkTo(methodOn(PostController.class).getPostsByUserId(post.getUserid(), null)).withRel("allPosts"));

    }

}