package com.rufino.server.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.data.domain.Page;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(value = { "page" })
public class PageResponse {

    private List<File> filesList;
    private Integer totalPages, pageNumber;
    private Page<File> page;

    public PageResponse() {
    }

    public PageResponse(Page<File> page) {
        this.page = page;
        this.filesList = page.toList();
        this.pageNumber = page.getNumber();
        this.totalPages = page.getTotalPages();
    }

    public List<File> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<File> filesList) {
        this.filesList = filesList;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Page<File> Page() {
        return page;
    }

    public void setPage(Page<File> page) {
        this.page = page;
    }

}