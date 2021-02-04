package com.rufino.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ServerApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void clearTable() {
		jdbcTemplate.update("DELETE FROM files");
	}

	@Test
	public void itShouldUploadAFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes());

		mockMvc.perform(multipart("/api/v1/file/save").file(file)).andExpect(status().isOk());
	}

	public void itShouldGetFiles() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes());

		mockMvc.perform(multipart("/api/v1/file/save").file(file)).andExpect(status().isOk());
	}

}