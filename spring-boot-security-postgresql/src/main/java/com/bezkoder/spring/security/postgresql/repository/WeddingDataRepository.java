// Repository
package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.interfaces.WeddingProjected;
import com.bezkoder.spring.security.postgresql.models.WeddingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface WeddingDataRepository extends JpaRepository<WeddingData, Long> {
    Optional<WeddingProjected> findByUuid(UUID uuid);





    // Additional CRUD methods can be directly used from JpaRepository:
    // - save(WeddingData entity): Saves or updates an entity.
    // - findById(Long id): Retrieves an entity by its ID.
    // - findAll(): Retrieves all entities.
    // - deleteById(Long id): Deletes an entity by its ID.
}