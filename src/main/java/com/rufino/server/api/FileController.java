package com.rufino.server.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.File;
import com.rufino.server.model.PageResponse;
import com.rufino.server.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("save")
    public File uploadFile(@RequestParam("file") MultipartFile file) {
        return fileService.saveFile(file);
    }

    @GetMapping
    public List<File> getAllItems() {
        return fileService.getAllFiles();
    }

    @GetMapping("page")
    public PageResponse getAllFiles(@RequestParam(name = "number", defaultValue = "0") int number,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return fileService.getPage(number, size);
    }

    @DeleteMapping("delete/{id}")
    public Map<String, String> deletFileById(@PathVariable String id) {
        Map<String, String> message = new HashMap<>();

        try {
            UUID fileId = UUID.fromString(id);
            int op = fileService.deleteFileById(fileId);
            if (op == 0)
                throw new ApiRequestException("File not found", HttpStatus.NOT_FOUND);
            message.put("message", "successfully operation");
            return message;
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid file UUID format", HttpStatus.BAD_REQUEST);
        }
    }

}
