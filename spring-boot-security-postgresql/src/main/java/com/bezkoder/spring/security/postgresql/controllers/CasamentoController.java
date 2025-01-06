package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.WeddingData;
import com.bezkoder.spring.security.postgresql.services.WeddingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;


@RestController
@RequestMapping("/api/casamento")
@CrossOrigin(
        origins = "*",
        allowedHeaders = {"Authorization", "Content-Type", "Accept"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowCredentials = "false",
        maxAge = 3600
)
public class CasamentoController {

    @Autowired
    private WeddingDataService weddingDataService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<WeddingData> saveWedding(
            @RequestParam("mediaType") String mediaType,
            @RequestParam("brideAndGroomName") String brideAndGroomName,
            @RequestParam("weddingDate") LocalDate weddingDate,
            @RequestParam("userId") Long userId,
            @RequestParam("color") String color,
            @RequestParam(value = "media", required = false) MultipartFile media) throws IOException {

        // Chama o serviço para salvar o post com os dados fornecidos
        WeddingData savedWeddingData = weddingDataService.saveWeddingMidia(mediaType, brideAndGroomName, weddingDate, userId, color, media);
        return ResponseEntity.ok(savedWeddingData);
    }


}
