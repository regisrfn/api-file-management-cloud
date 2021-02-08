package com.rufino.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rufino.server.model.File;
import com.rufino.server.model.PageResponse;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ServerApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

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

	@Test
	public void itShouldGetFiles() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes());

		mockMvc.perform(multipart("/api/v1/file/save").file(file)).andExpect(status().isOk()).andReturn();
		MvcResult result = mockMvc.perform(get("/api/v1/file").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		List<File> orderList = Arrays
				.asList(objectMapper.readValue(result.getResponse().getContentAsString(), File[].class));

		assertThat(orderList.size()).isEqualTo(1);

		mockMvc.perform(multipart("/api/v1/file/save").file(file)).andExpect(status().isOk()).andReturn();
		result = mockMvc.perform(get("/api/v1/file").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();

		orderList = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), File[].class));
		assertThat(orderList.size()).isEqualTo(2);
	}

	@Test
	public void itShouldGetFilesPage() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes());
		mockMvc.perform(multipart("/api/v1/file/save").file(file)).andExpect(status().isOk()).andReturn();

		MvcResult result = mockMvc.perform(get("/api/v1/file/page").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		PageResponse pageResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
				PageResponse.class);

		assertThat(pageResponse.getFilesList().size()).isEqualTo(1);

		mockMvc.perform(multipart("/api/v1/file/save").file(file)).andExpect(status().isOk()).andReturn();
		result = mockMvc.perform(get("/api/v1/file/page?number=1&size=1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		pageResponse = objectMapper.readValue(result.getResponse().getContentAsString(), PageResponse.class);

		assertThat(pageResponse.getFilesList().size()).isEqualTo(1);
		assertThat(pageResponse.getTotalPages()).isEqualTo(2);
		assertThat(pageResponse.getPageNumber()).isEqualTo(1);
	}

	@Test
	public void itShouldDeleteFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes());

		MvcResult result = mockMvc.perform(multipart("/api/v1/file/save").file(file)).andExpect(status().isOk())
				.andReturn();

		File fileResponse = objectMapper.readValue(result.getResponse().getContentAsString(), File.class);
		result = mockMvc.perform(delete("/api/v1/file/delete/" + fileResponse.getFileId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("successfully operation")))
				.andExpect(status().isOk()).andExpect(status().isOk()).andReturn();
	}

}