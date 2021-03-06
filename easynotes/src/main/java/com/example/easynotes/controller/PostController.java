package com.example.easynotes.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.easynotes.Exception.ResourceNotFoundException;
import com.example.easynotes.model.Post;
import com.example.easynotes.repository.PostRepository;


@RestController
public class PostController {
	private static final Logger logger = LogManager.getLogger(PostController.class);
	@Autowired
	PostRepository postRepository;

	@GetMapping("/posts")
	public Page<Post> getAllPosts(Pageable pageable) {
		return postRepository.findAll(pageable);

	}

	@GetMapping("/posts/{postId}")
	public Post getPostById(@PathVariable(value = "postId") Long postId) {
    logger.info("Info log");
		return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post " + postId + " not found"));
	}

	@PostMapping("/posts")
	public Post creatPost(@Valid @RequestBody Post post) {
		return postRepository.save(post);
	}

	@PutMapping("/posts/{postId}")
	public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
		return postRepository.findById(postId).map(post -> {
			post.setTitle(postRequest.getTitle());
			post.setContent(postRequest.getContent());
			post.setDescription(postRequest.getDescription());
			return postRepository.save(post);
		}).orElseThrow(() -> new ResourceNotFoundException("Post " + postId + " not found"));

	}

	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable Long postId) {
		return postRepository.findById(postId).map(post -> {
			postRepository.delete(post);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("Post " + postId + " not found"));
	}
}
