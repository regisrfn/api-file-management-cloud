package com.rufino.server.dao;

import java.util.List;
import java.util.UUID;

import com.rufino.server.model.File;

public interface FileDao {
    File insertFile(File item);

    int deleteFile(UUID id);

    List<File> getAll();

    File getFile(UUID id);

    File updateFile(UUID id, File item);
}
