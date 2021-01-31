package com.rufino.server.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.rufino.server.exception.ApiRequestException;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService{

    private final Path root = Paths.get("uploads");

    
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new ApiRequestException("Could not initialize folder for upload!");
        }
    }

    
    public String save(MultipartFile file) {
        try {
            Path filePath = this.root.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath);
            return filePath.toString();
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    
    public boolean delete(String filename) {
        Path filePath = this.root.resolve(filename);
        try {
            Files.deleteIfExists(filePath);
            return true;
        } catch (IOException e) {
            return false;
        }
       
    }

}