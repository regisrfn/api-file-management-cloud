package com.rufino.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.FileDao;
import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.File;
import com.rufino.server.model.FileResponse;
import com.rufino.server.model.PageResponse;
import com.rufino.server.repository.FileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class FileService {

    private FileDao fileDao;
    private RestTemplate restTemplate;
    private FileRepository fileRepository;
    private String apiUrl;
    private Dotenv dotenv;

    @Autowired
    public FileService(FileDao fileDao, FileRepository fileRepository, RestTemplate restTemplate) {
        dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.apiUrl = dotenv.get("API_UPLOAD_URL");
        this.restTemplate = restTemplate;
        this.fileDao = fileDao;
        this.fileRepository = fileRepository;
    }

    public File saveFile(MultipartFile file) {
        File newFile = new File();

        newFile.setFileName(file.getOriginalFilename());
        newFile.setFileContentType(file.getContentType());
        newFile.setFileSize(file.getSize());

        try {
            FileResponse fileResponse = uploadFile(file);
            newFile.setFileUrl(fileResponse.getUrl());
            return fileRepository.insertFile(newFile);

        } catch (HttpServerErrorException e) {
            e.printStackTrace();
            throw new ApiRequestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiRequestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public List<File> saveAllFiles(MultipartFile[] files) {
        List<File> fList = new ArrayList<>();
        try {
            Arrays.asList(files).stream().forEach(file -> {
                File newFile = new File();
                newFile.setFileName(file.getOriginalFilename());
                newFile.setFileContentType(file.getContentType());
                newFile.setFileSize(file.getSize());
                FileResponse fileResponse = uploadFile(file);
                newFile.setFileUrl(fileResponse.getUrl());
                fList.add(newFile);
            });
            return fileRepository.saveAll(fList);

        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            File file = getFileById(id);
            if (file == null)
                throw new ApiRequestException("File not found", HttpStatus.NOT_FOUND);
            List<String> split = Arrays.asList(file.getFileUrl().split("/"));
            restTemplate.delete(apiUrl + "/delete/" + split.get(split.size() - 1));
            return fileDao.deleteFile(id);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public File updateFile(UUID id, File file) {
        try {
            return fileDao.updateFile(id, file);
        } catch (DataIntegrityViolationException e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    public PageResponse getPage(int page, int size) {
        try {
            return new PageResponse(fileDao.getFilesPage(page, size));
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }

    }

    private FileResponse uploadFile(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        FileResponse response = restTemplate.postForObject(apiUrl + "/upload", request, FileResponse.class);
        return response;
    }
}
