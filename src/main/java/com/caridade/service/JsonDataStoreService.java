package com.caridade.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Service
public class JsonDataStoreService {

    private final ObjectMapper objectMapper;
    private final Path dataDirectory;

    public JsonDataStoreService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy().findAndRegisterModules();
        this.dataDirectory = Paths.get("data");
        criarDiretorioSeNecessario();
    }

    public <T> List<T> readList(String fileName, TypeReference<List<T>> typeReference) {
        Path filePath = resolveFile(fileName);

        if (Files.notExists(filePath)) {
            return Collections.emptyList();
        }

        try {
            if (Files.size(filePath) == 0) {
                return Collections.emptyList();
            }

            return objectMapper.readValue(filePath.toFile(), typeReference);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler o arquivo JSON: " + filePath, e);
        }
    }

    public <T> void writeList(String fileName, List<T> data) {
        Path filePath = resolveFile(fileName);

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), data);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao salvar o arquivo JSON: " + filePath, e);
        }
    }

    private Path resolveFile(String fileName) {
        criarDiretorioSeNecessario();
        return dataDirectory.resolve(fileName);
    }

    private void criarDiretorioSeNecessario() {
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível criar a pasta data", e);
        }
    }
}