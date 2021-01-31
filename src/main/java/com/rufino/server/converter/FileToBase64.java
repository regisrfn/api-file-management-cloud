package com.rufino.server.converter;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

public class FileToBase64 {

    byte[] fileContent;

    public FileToBase64(String filePath) throws IOException {
        this.fileContent = FileUtils.readFileToByteArray(new File(filePath));
    }

    public String convert() throws IOException {
        return Base64.getEncoder().encodeToString(fileContent);
    }

}
