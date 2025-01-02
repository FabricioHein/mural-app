// Service
package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.interfaces.WeddingProjected;

import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.WeddingData;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.WeddingDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WeddingDataService {
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @Autowired
    private WeddingDataRepository weddingDataRepository;

    @Autowired
    private UserRepository userRepository;

    public List<WeddingData> findAll() {
        return weddingDataRepository.findAll();
    }

    public Optional<WeddingData> findById(Long id) {
        return weddingDataRepository.findById(id);
    }

    public Optional<WeddingData> findByUuid(UUID uuid) {
        return weddingDataRepository.findByUuid(uuid);
    }
    public WeddingData saveWeddingMidia(
            String mediaType,
            String brideAndGroomName,
            LocalDate weddingDate,
            Long userId,
            String color,
            MultipartFile media
    ) throws IOException {
        try {
            // Busca o usuário associado ao userId
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Verifica se já existe um WeddingData para o usuário
            WeddingData wedding = weddingDataRepository.findByUserId(userId)
                    .orElseGet(() -> {
                        WeddingData // Caso não exista, cria um novo WeddingData
                         newWedding = new WeddingData();
                        newWedding.setUuid(UUID.randomUUID());
                        newWedding.setUser(user);
                        return newWedding;
                    });

            // Atualiza os campos do objeto WeddingData
            wedding.setBrideAndGroomName(brideAndGroomName);
            wedding.setColor(color);
            wedding.setWeddingDate(weddingDate);

            if (media != null && !media.isEmpty()) {
                // Verifica e obtém a extensão do arquivo com base no tipo MIME
                String fileExtension = getFileExtensionFromMediaType(mediaType);
                if (fileExtension == null) {
                    throw new IllegalArgumentException("Tipo de mídia não suportado: " + mediaType);
                }

                // Gera o nome do arquivo
                String fileName = generateFileName(fileExtension);

                // Cria o diretório de upload, se não existir
                File uploadFolder = new File(UPLOAD_DIR);
                if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
                    throw new IOException("Não foi possível criar o diretório de upload: " + UPLOAD_DIR);
                }

                // Define o caminho completo para o arquivo
                String filePath = UPLOAD_DIR + File.separator + fileName;

                // Salva o arquivo no sistema de arquivos
                File destinationFile = new File(filePath);
                media.transferTo(destinationFile);

                // Define o caminho do arquivo no objeto WeddingData
                wedding.setImageUrl(fileName);
            }

            // Salva o WeddingData (atualizado ou criado) no banco de dados
            return weddingDataRepository.save(wedding);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar mídia: " + e.getMessage(), e);
        }
    }


    /**
     * Retorna a extensão do arquivo com base no tipo MIME.
     *
     * @param mediaType o tipo MIME da mídia (ex: "image/jpeg", "video/mp4")
     * @return a extensão do arquivo correspondente ou null se o tipo não for suportado
     */
    private String getFileExtensionFromMediaType(String mediaType) {
        switch (mediaType) {
            case "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "video/mp4":
                return "mp4";
            case "image/gif":
                return "gif";
            default:
                return null; // Tipo não suportado
        }
    }

    /**
     * Gera um nome único para o arquivo.
     *
     * @param fileExtension a extensão do arquivo (ex: "jpg", "mp4")
     * @return o nome do arquivo gerado
     */
    private String generateFileName(String fileExtension) {
        return UUID.randomUUID().toString() + "." + fileExtension;
    }


}