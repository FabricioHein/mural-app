package com.bezkoder.spring.security.postgresql.controllers;


import com.bezkoder.spring.security.postgresql.dto.PostDTO;
import com.bezkoder.spring.security.postgresql.models.Post;


import com.bezkoder.spring.security.postgresql.payload.request.PostRequest;
import com.bezkoder.spring.security.postgresql.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(
        origins = "https://muralnoivos.web.app", // Substitua pela URL do seu frontend
        allowedHeaders = {"Authorization", "Content-Type", "Accept"}, // Cabeçalhos permitidos
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, // Métodos permitidos
        allowCredentials = "true" // Permite credenciais (cookies, tokens, etc.)
)

public class PostController {

    @Autowired
    private PostService postService;

    // Retorna todos os posts
    @GetMapping("/{weddingDataId}")
    public ResponseEntity<List<PostDTO>> getAllPosts(@PathVariable Long weddingDataId) {
        try {
            List<Post> posts = postService.getAllPosts(weddingDataId);
            if (posts.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<PostDTO> postDTOs = posts.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(postDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private PostDTO convertToDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getContent(),
              "http://localhost:3000/uploads/" + post.getMediaUrl(),
                post.getReactions(),
                post.getUser()
        );
    }

    // Adiciona uma reação a um post
    @PostMapping("/{postId}/reactions")
    public ReactionResponse addReaction(@PathVariable Long postId, @RequestBody String reaction) {
        Integer reactionCount = postService.addReaction(postId, reaction);
        return new ReactionResponse(reaction, reactionCount);
    }

    @PostMapping(value = "/midia", consumes = "multipart/form-data")
    public ResponseEntity<Post> savePostWithDetails(
            @RequestParam("mediaType") String mediaType,
            @RequestParam("userId") Long userId,
            @RequestParam("weddingDataId") Long weddingDataId,
            @RequestParam("content") String content,
            @RequestParam(value = "media", required = false) MultipartFile media) throws IOException {

        // Chama o serviço para salvar o post com os dados fornecidos
        Post savedPost = postService.savePostMidia(content, media, mediaType, userId, weddingDataId);
        return ResponseEntity.ok(savedPost);
    }

    // Salva um post diretamente com JSON (application/json)
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Post> savePostWithJson(@RequestBody PostRequest postRequest) {
        Post savedPost = postService.savePost(postRequest);
        return ResponseEntity.ok(savedPost);
    }


    // Classe de resposta para as reações
    public static class ReactionResponse {
        private String reaction;
        private Integer reactionCount;

        public ReactionResponse(String reaction, Integer reactionCount) {
            this.reaction = reaction;
            this.reactionCount = reactionCount;
        }

        public String getReaction() {
            return reaction;
        }

        public void setReaction(String reaction) {
            this.reaction = reaction;
        }

        public Integer getReactionCount() {
            return reactionCount;
        }

        public void setReactionCount(Integer reactionCount) {
            this.reactionCount = reactionCount;
        }
    }
}