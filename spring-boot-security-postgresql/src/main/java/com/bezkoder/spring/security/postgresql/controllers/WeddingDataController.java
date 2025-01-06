package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.WeddingData;
import com.bezkoder.spring.security.postgresql.services.WeddingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/wedding-data")
@CrossOrigin(
        origins = "*",
        allowedHeaders = {"Authorization", "Content-Type", "Accept"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowCredentials = "false",
        maxAge = 3600
)
public class WeddingDataController {

    @Autowired
    private WeddingDataService weddingDataService;


    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<WeddingData> getWeddingDataByUuid(@PathVariable UUID uuid) {
        Optional<WeddingData> weddingDataOptional = weddingDataService.findByUuid(uuid);

        if (weddingDataOptional.isPresent()) {
            WeddingData weddingData = weddingDataOptional.get();

            // Adiciona o prefixo ao imageUrl
            if (weddingData.getImageUrl() != null) {
                String baseUrl = "https://mural-app.onrender.com/uploads/";
                weddingData.setImageUrl(baseUrl + weddingData.getImageUrl());
            }

            return ResponseEntity.ok(weddingData);
        }

        return ResponseEntity.notFound().build();
    }


}
