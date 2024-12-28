package com.bezkoder.spring.security.postgresql.services;


import com.bezkoder.spring.security.postgresql.models.Post;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.WeddingData;
import com.bezkoder.spring.security.postgresql.payload.request.PostRequest;
import com.bezkoder.spring.security.postgresql.repository.PostRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.WeddingDataRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    private UserRepository userRepository;
    private WeddingDataRepository weddingDataRepository;


    private static final String UPLOAD_DIR = "/uploads/";

    // Obtém todos os posts
    public List<Post> getAllPosts(Long weddingDataId) {
        return postRepository.findByWeddingDataId(weddingDataId);
    }

    // Adiciona uma reação a um post
    public Integer addReaction(Long postId, String reaction) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.getReactions().merge(reaction, 1, Integer::sum);
            postRepository.save(post);
            return post.getReactions().get(reaction);
        }

        throw new RuntimeException("Post não encontrado");
    }

    public Post savePostMidia(String content, MultipartFile media) throws IOException {
        Post post = new Post();
        post.setContent(content);

        if (media != null && !media.isEmpty()) {
            // Garante que o arquivo seja salvo corretamente no diretório
            String fileName = media.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);

            // Salva o arquivo no diretório
            Files.copy(media.getInputStream(), path);

            // Atualiza o post com o caminho da mídia
            post.setMediaUrl(UPLOAD_DIR + fileName);
            post.setMediaFolder(UPLOAD_DIR);
            post.setMediaType(media.getContentType());
        }

        return postRepository.save(post);
    }

    // Salva um post com JSON
    public Post savePost(PostRequest postRequest) {
        Post savedPost = new Post();

        // Mapeamento dos campos do PostRequest para a entidade Post
        savedPost.setContent(postRequest.getContent());
        savedPost.setMediaUrl(postRequest.getMediaUrl());
        savedPost.setMediaFolder(postRequest.getMediaFolder());
        savedPost.setMediaType(postRequest.getMediaType());
        savedPost.setUser(postRequest.getUser());
        savedPost.setWeddingData(postRequest.getWeddingData());

        // Salva o Post no repositório
        return postRepository.save(savedPost);
    }
}
