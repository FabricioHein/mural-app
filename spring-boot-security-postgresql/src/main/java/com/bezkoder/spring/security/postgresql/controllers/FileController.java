package com.bezkoder.spring.security.postgresql.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@CrossOrigin(
        origins = "https://muralnoivos.web.app",
        allowedHeaders = {"Authorization", "Content-Type", "Accept"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowCredentials = "true",
        maxAge = 3600
)
public class FileController {

    private static final String UPLOAD_DIR = "uploads";  // Caminho relativo

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
                return ResponseEntity.status(403).body(null);  // Acesso negado ao arquivo
            }

            Resource resource = new UrlResource(filePath.toUri());

            // Verifica se o arquivo não existe ou não pode ser lido
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(404).build();  // Arquivo não encontrado
            }

            // Detecta o tipo de conteúdo do arquivo
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Tipo padrão
            }

            // Retorna o arquivo com os headers apropriados
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.status(500).body(null);  // Erro ao carregar o arquivo
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);  // Erro ao processar o arquivo
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);  // Erro de segurança (acesso negado)
        }
    }
}

