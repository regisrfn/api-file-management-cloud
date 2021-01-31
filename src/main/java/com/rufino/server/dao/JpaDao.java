package com.rufino.server.dao;

import java.util.UUID;

import com.rufino.server.model.File;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDao extends JpaRepository<File, UUID> {
}
