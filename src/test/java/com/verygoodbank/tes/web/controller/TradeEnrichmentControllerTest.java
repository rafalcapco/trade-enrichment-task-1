package com.verygoodbank.tes.web.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verygoodbank.tes.web.service.TradeDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(TradeEnrichmentController.class)
class TradeEnrichmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TradeDataService tradeDataService;

    @Test
    public void smallFileTest() throws Exception {
        String content = """
            date,product_id,currency,price
            20160101,1,EUR,10.0
            20160101,2,EUR,20.1
            20160101,3,EUR,30.34
            20160101,11,EUR,35.34
            20asd160101,11,EUR,35.34""";
        MockMultipartFile file
            = new MockMultipartFile(
            "file",
            "hello.csv",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            content.getBytes()
        );

        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/enrich")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM));
    }
}