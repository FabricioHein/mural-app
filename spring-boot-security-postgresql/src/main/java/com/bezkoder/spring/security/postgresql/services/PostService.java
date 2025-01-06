package com.bezkoder.spring.security.postgresql.services;


import com.bezkoder.spring.security.postgresql.models.Post;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.WeddingData;
import com.bezkoder.spring.security.postgresql.payload.request.PostRequest;
import com.bezkoder.spring.security.postgresql.repository.PostRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.WeddingDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.io.File;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WeddingDataRepository weddingDataRepository;


    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

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

    public Post savePostMidia(String content, MultipartFile media, String mediaType, Long userId, Long weddingDataId) throws IOException {
        try {
            Post post = new Post();
            post.setContent(content);
            post.setMediaType(mediaType);

            // Associa o usuário ao post
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            post.setUser(user);

            // Associa os dados do casamento ao post
            WeddingData weddingData = weddingDataRepository.findById(weddingDataId)
                    .orElseThrow(() -> new RuntimeException("Dados do casamento não encontrados"));
            post.setWeddingData(weddingData);

            if (media != null && !media.isEmpty()) {
                // Verificar o tipo de mídia (foto ou vídeo) e ajustar a extensão do arquivo com base no MIME
                String fileExtension = getFileExtensionFromMediaType(mediaType);
                String fileName = generateFileName(fileExtension);

                // Cria o diretório de upload, se não existir
                File uploadFolder = new File(UPLOAD_DIR);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs(); // Cria a pasta de upload, se necessário
                }

                String filePath = UPLOAD_DIR + fileName;

                // Salvar o arquivo no sistema de arquivos
                media.transferTo(new File(filePath));  // Aqui é onde o "Blob" é convertido e salvo como um arquivo real.

                // Definir o caminho e o nome do arquivo no post
                post.setMediaUrl(fileName);
                post.setMediaFolder(UPLOAD_DIR);
            }

            return postRepository.save(post);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar mídia: " + e.getMessage(), e);
        }
    }
    // Método para obter a extensão do arquivo com base no tipo MIME
    private String getFileExtensionFromMediaType(String mediaType) {
        switch (mediaType) {
            case "image/png":
                return ".png";
            case "png":
                return ".png";
            case "image/jpeg":
                return ".jpg";
            case "video/mp4":
                return ".mp4";
            case "video/webm":
                return ".webm";
            case "webm":
                return ".webm";
            default:
                throw new RuntimeException("Tipo de mídia não suportado: " + mediaType);
        }
    }

    // Método para gerar um nome único para o arquivo
    private String generateFileName(String fileExtension) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        return "media_" + timeStamp + fileExtension; // Exemplo: media_1616161616161.png
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
