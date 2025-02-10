package com.hackerrank.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.api.model.Scan;
import com.hackerrank.api.repository.ScanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
class ScanControllerTest {
    ObjectMapper om = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ScanRepository repository;

    @Test
    public void testCreation() throws Exception {
        Scan expectedRecord = Scan.builder().domainName("java.com").build();
        Scan actualRecord = om.readValue(mockMvc.perform(post("/scan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Scan.class);

        Assertions.assertEquals(expectedRecord.getDomainName(), actualRecord.getDomainName());
    }

    @Test
    public void shouldGetSanById() throws Exception {
        mockMvc.perform(get("/scan/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void shouldThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/scan/{id}", 1000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource not found with id 1000"));
    }

    @Test
    public void shouldDeleteScan() throws Exception {
        mockMvc.perform(delete("/scan/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailDeleteScan() throws Exception {
        mockMvc.perform(delete("/scan/{id}", 1000))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource not found with id 1000"));
    }

    @Test
    public void shouldSearch() throws Exception {
        mockMvc.perform(get("/scan/search/{domainName}", "domain1.com", "numPages")
                        .param("orderBy", "numPages").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].domainName", is("domain1.com")));
    }

    @Test
    public void shouldFailSearch() throws Exception {
        mockMvc.perform(get("/scan/search/{domainName}", "invalidDomainName")
                        .param("orderBy", "numPages").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
