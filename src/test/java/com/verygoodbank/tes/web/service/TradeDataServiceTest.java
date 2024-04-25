package com.verygoodbank.tes.web.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.verygoodbank.tes.web.config.ProductCatalog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class TradeDataServiceTest {

    private final ProductCatalog productCatalog = Mockito.mock(ProductCatalog.class);

    private TradeDataService subject = new TradeDataService(productCatalog);

    @Test
    void successfullyEnrichData() throws IOException {
        // given
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
        Mockito.when(productCatalog.getProducts()).thenReturn(Map.of(
            "1", "Treasury Bills Domestic",
            "2", "Corporate Bonds Domestic",
            "3", "REPO Domestic"));

        // when
        InputStreamResource inputStreamResource = subject.enrichData(file);

        // then
        String actual = new BufferedReader(new InputStreamReader(inputStreamResource.getInputStream()))
            .lines().parallel().collect(Collectors.joining("\n"));
        String expected = """
            date,product_id,currency,price
            20160101,Treasury Bills Domestic,EUR,10.0
            20160101,Corporate Bonds Domestic,EUR,20.1
            20160101,REPO Domestic,EUR,30.34
            20160101,Missing Product Name,EUR,35.34""";
        assertEquals(expected, actual);
    }

    @Test
    void processEmptyFile() throws IOException {
        // given
        MockMultipartFile file
            = new MockMultipartFile(
            "file",
            "hello.csv",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            new byte[0]
        );
        Mockito.when(productCatalog.getProducts()).thenReturn(Map.of(
            "1", "Treasury Bills Domestic",
            "2", "Corporate Bonds Domestic",
            "3", "REPO Domestic"));

        // when
        InputStreamResource inputStreamResource = subject.enrichData(file);

        // then
        String actual = new BufferedReader(new InputStreamReader(inputStreamResource.getInputStream()))
            .lines().parallel().collect(Collectors.joining("\n"));
        String expected = """
            """;
        assertEquals(expected, actual);
    }
}