package com.verygoodbank.tes;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = TradeEnrichmentServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
public class TradeEnrichmentServiceApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void mediumFileTest() throws Exception {
        File resource = new ClassPathResource(
            "mediumTrade.csv").getFile();
        MockMultipartFile file
            = new MockMultipartFile(
            "file",
            "hello.csv",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            Files.readAllBytes(resource.toPath())
        );

        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/enrich")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM));
    }
}
