// Service
package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.interfaces.WeddingProjected;
import com.bezkoder.spring.security.postgresql.models.WeddingData;
import com.bezkoder.spring.security.postgresql.repository.WeddingDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WeddingDataService {

    @Autowired
    private WeddingDataRepository weddingDataRepository;

    public List<WeddingData> findAll() {
        return weddingDataRepository.findAll();
    }

    public Optional<WeddingData> findById(Long id) {
        return weddingDataRepository.findById(id);
    }

    public Optional<WeddingProjected> findByUuid(UUID uuid) {
        return weddingDataRepository.findByUuid(uuid);
    }

    public WeddingData save(WeddingData weddingData) {
        return weddingDataRepository.save(weddingData);
    }


    public void deleteById(Long id) {
        weddingDataRepository.deleteById(id);
    }
}