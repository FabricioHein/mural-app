package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.interfaces.WeddingProjected;
import com.bezkoder.spring.security.postgresql.models.WeddingData;
import com.bezkoder.spring.security.postgresql.services.WeddingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/wedding-data")
@CrossOrigin(origins = "*")
public class WeddingDataController {

    @Autowired
    private WeddingDataService weddingDataService;

    @GetMapping
    public ResponseEntity<List<WeddingData>> getAllWeddingData() {
        List<WeddingData> weddingDataList = weddingDataService.findAll();
        return ResponseEntity.ok(weddingDataList);
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<WeddingProjected> getWeddingDataByUuid(@PathVariable UUID uuid) {
        Optional<WeddingProjected> weddingData = weddingDataService.findByUuid(uuid);
        return weddingData.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WeddingData> createWeddingData(@RequestBody WeddingData weddingData) {
        weddingData.setUuid(UUID.randomUUID());
        WeddingData savedWeddingData = weddingDataService.save(weddingData);
        return ResponseEntity.ok(savedWeddingData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeddingData> updateWeddingData(@PathVariable Long id, @RequestBody WeddingData weddingDataDetails) {
        Optional<WeddingData> optionalWeddingData = weddingDataService.findById(id);

        if (optionalWeddingData.isPresent()) {
            WeddingData existingWeddingData = optionalWeddingData.get();

            existingWeddingData.setBrideAndGroomName(weddingDataDetails.getBrideAndGroomName());
            existingWeddingData.setImageUrl(weddingDataDetails.getImageUrl());
            existingWeddingData.setLeaveMessageUrl(weddingDataDetails.getLeaveMessageUrl());
            existingWeddingData.setViewMuralUrl(weddingDataDetails.getViewMuralUrl());
            existingWeddingData.setWeddingDate(weddingDataDetails.getWeddingDate());
            existingWeddingData.setColor(weddingDataDetails.getColor());
            existingWeddingData.setUser(weddingDataDetails.getUser());

            WeddingData updatedWeddingData = weddingDataService.save(existingWeddingData);
            return ResponseEntity.ok(updatedWeddingData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeddingData(@PathVariable Long id) {
        weddingDataService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
