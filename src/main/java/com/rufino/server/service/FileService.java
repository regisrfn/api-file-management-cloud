package com.rufino.server.service;

import java.util.List;
import java.util.UUID;

import com.rufino.server.converter.FileToBase64;
import com.rufino.server.dao.FileDao;
import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.File;
import com.rufino.server.model.FileResponse;
import com.rufino.server.repository.FileRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class FileService {

    private FileDao fileDao;
    private FileStorageService storageService;
    private RestTemplate restTemplate;
    private FileRepository fileRepository;
    String apiUrl;
    private Dotenv dotenv;

    @Autowired
    public FileService(FileDao fileDao, FileStorageService storageService, FileRepository fileRepository) {
        dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.restTemplate = new RestTemplate();
        this.fileDao = fileDao;
        this.storageService = storageService;
        this.fileRepository = fileRepository;
        this.apiUrl = dotenv.get("API_UPLOAD_URL");
    }

    public File saveFile(MultipartFile file) {
        File newFile = new File();

        newFile.setFileName(file.getOriginalFilename());
        newFile.setFileContentType(file.getContentType());
        newFile.setFileSize(file.getSize());

        try {
            String savedFile = storageService.save(file);
            FileToBase64 fileToBase64 = new FileToBase64(savedFile);
            String encodedString = fileToBase64.convert();

            JSONObject fileObj = new JSONObject();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            fileObj.put("name", newFile.getFileName());
            fileObj.put("encodedString", encodedString);

            HttpEntity<String> request = new HttpEntity<String>(fileObj.toString(), headers);
            FileResponse response = restTemplate.postForObject(apiUrl, request, FileResponse.class);

            newFile.setFileUrl(response.getUrl());

            storageService.delete(file.getOriginalFilename());
            return fileRepository.insertFile(newFile);

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public List<File> getAllFiles() {
        return fileDao.getAll();
    }

    public File getFileById(UUID id) {
        return fileDao.getFile(id);
    }

    public int deleteFileById(UUID id) {
        return fileDao.deleteFile(id);
    }

    public File updateFile(UUID id, File file) {
        try {
            return fileDao.updateFile(id, file);
        } catch (DataIntegrityViolationException e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
