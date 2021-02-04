package com.rufino.server.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "files")
@JsonInclude(Include.NON_NULL)
public class File {

    @Id
    private UUID fileId;

    @NotBlank(message = "Value should not be empty")
    private String fileName;

    @NotNull(message = "Value should not be empty")
    private Long fileSize;

    @NotBlank(message = "Value should not be empty")
    private String fileContentType;

    @NotBlank(message = "Value should not be empty")
    private String fileUrl;

    @NotNull(message = "Value should not be empty")
    @Column(columnDefinition = "timestamp with time zone")
    private ZonedDateTime fileCreatedAt;

    @Column(columnDefinition = "text")
    private String fileDescription;
    private UUID userId;

    public File() {
        setFileCreatedAt(ZonedDateTime.now(ZoneId.of("Z")));
        setFileId(UUID.randomUUID());
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public ZonedDateTime getFileCreatedAt() {
        return fileCreatedAt;
    }

    public void setFileCreatedAt(ZonedDateTime fileCreatedAt) {
        this.fileCreatedAt = fileCreatedAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setUserId(String userId) {
        try {
            this.userId = UUID.fromString(userId);
        } catch (Exception e) {
            //TODO: handle exception
        }        
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

      

}
