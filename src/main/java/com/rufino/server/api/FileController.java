package com.rufino.server.api;

import java.util.List;

import com.rufino.server.model.File;
import com.rufino.server.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class FileController {

    FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("upload")
    public File uploadFile(@RequestParam("file") MultipartFile file) {
        return fileService.saveFile(file);
    }

    @GetMapping("uploads")
    public List<File> getAllItems() {
        return fileService.getAllFiles();
    }

}
