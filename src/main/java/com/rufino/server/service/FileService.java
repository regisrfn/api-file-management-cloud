package com.rufino.server.service;

import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.FileDao;
import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.File;
import com.rufino.server.model.FileResponse;
import com.rufino.server.repository.FileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class FileService {

    private FileDao fileDao;
    private RestTemplate restTemplate;
    private FileRepository fileRepository;
    String apiUrl;
    private Dotenv dotenv;
    private SimpleClientHttpRequestFactory requestFactory;

    @Autowired

    public FileService(FileDao fileDao, FileRepository fileRepository) {
        dotenv = Dotenv.configure().ignoreIfMissing().load();
        
        requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        requestFactory.setChunkSize(4096);
        requestFactory.setConnectTimeout(0);

        this.restTemplate = new RestTemplate(requestFactory);
        this.fileDao = fileDao;
        this.fileRepository = fileRepository;
        this.apiUrl = dotenv.get("API_UPLOAD_URL");
    }

    public File saveFile(MultipartFile file) {
        File newFile = new File();

        newFile.setFileName(file.getOriginalFilename());
        newFile.setFileContentType(file.getContentType());
        newFile.setFileSize(file.getSize());

        try {
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();

            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            body.add("file", file.getResource());

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            FileResponse response = restTemplate.postForObject(apiUrl, request, FileResponse.class);

            newFile.setFileUrl(response.getUrl());

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
