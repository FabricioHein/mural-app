package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.interfaces.PostProjection;
import com.bezkoder.spring.security.postgresql.models.Post;
import com.bezkoder.spring.security.postgresql.models.WeddingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Consulta SQL nativa para buscar posts por weddingDataId
    @Query(value = "SELECT * FROM post WHERE wedding_data_id = :weddingDataId ORDER BY post.id DESC", nativeQuery = true)
    List<Post> findByWeddingDataId(Long weddingDataId);

}
