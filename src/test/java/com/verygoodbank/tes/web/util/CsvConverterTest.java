package com.verygoodbank.tes.web.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class CsvConverterTest {

    @Test
    void successfullyConvertFileToList() throws IOException {
        // given
        File resource = new ClassPathResource(
            "product.csv").getFile();
        // when
        List<List<String>> result = CsvConverter.readCsv(resource);
        // then
        assertEquals(11, result.size());
        assertEquals(2, result.get(0).size());
    }

    @Test
    void successfullyParseEmptyFile() throws IOException {
        // given
        File resource = new ClassPathResource(
            "emptyTradeFile.csv").getFile();
        // when
        List<List<String>> result = CsvConverter.readCsv(resource);
        // then
        assertEquals(0, result.size());
    }
}