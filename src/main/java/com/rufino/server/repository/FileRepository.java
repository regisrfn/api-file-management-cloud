package com.rufino.server.repository;

import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.FileDao;
import com.rufino.server.dao.JpaDao;
import com.rufino.server.model.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepository implements FileDao {

    private JpaDao jpaDataAccess;

    @Autowired
    public FileRepository(JpaDao jpaDataAccess) {
        this.jpaDataAccess = jpaDataAccess;

    }

    @Override
    public File insertFile(File file) {
        return jpaDataAccess.save(file);
    }

    @Override
    public int deleteFile(UUID id) {
        try {
            jpaDataAccess.deleteById(id);
            return 1;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public List<File> getAll() {
        return jpaDataAccess.findAll();
    }

    @Override
    public File getFile(UUID id) {
        return jpaDataAccess.findById(id).orElse(null);
    }

    @Override
    public File updateFile(UUID id, File file) {
        file.setFileId(id);
        return jpaDataAccess.findById(id).orElse(null);
    }

    @Override
    public Page<File> getFilesPage(int pageNumber, int size) {
        Sort sort = Sort.by("fileCreatedAt").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, size, sort);
        return jpaDataAccess.findAll(pageRequest);
    }

}
