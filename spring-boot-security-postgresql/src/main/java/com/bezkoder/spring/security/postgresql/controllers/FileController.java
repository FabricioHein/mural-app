package com.bezkoder.spring.security.postgresql.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {

    private static final String UPLOAD_DIR = "uploads";  // Caminho relativo é mais seguro

    /**
     * Método para servir arquivos da pasta de uploads.
     *
     * @param fileName Nome do arquivo solicitado.
     * @return Resposta HTTP com o arquivo como recurso.
     */
    @GetMapping("/uploads/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
            // Cria o diretório de uploads se não existir
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // Resolve o caminho completo do arquivo
            Path filePath = uploadPath.resolve(fileName).normalize();

            // Verifica se o arquivo está dentro do diretório de uploads (segurança)
            if (!filePath.toAbsolutePath().startsWith(uploadPath)) {
                throw new SecurityException("Acesso negado ao arquivo: " + fileName);
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Detecta o tipo de conteúdo do arquivo
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Configura os headers para download
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro ao carregar o arquivo: " + fileName, e);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo: " + fileName, e);
        } catch (SecurityException e) {
            throw new RuntimeException("Erro de segurança: " + e.getMessage(), e);
        }
    }
}