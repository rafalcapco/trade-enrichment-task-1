package com.verygoodbank.tes.web.util;

import com.verygoodbank.tes.web.exception.InvalidFileException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CsvConverter {

    private static final Logger LOG = LoggerFactory.getLogger(CsvConverter.class);

    private static final String COMMA_DELIMITER = ",";

    private CsvConverter() {
        //Empty private constructor to prevent instance creation
    }

    public static List<List<String>> readCsv(File file) {
        LOG.info("Attempting to read csv file: {}", file.getName());
        List<List<String>> csvContent = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                csvContent.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            LOG.error("Failed running readCsv for {}", file.getName(), e);
            throw new InvalidFileException(String.format("Failed running readCsv for %s", file.getName()), e);
        }
        LOG.info("Successfully read csv file: {}", file.getName());
        return csvContent;
    }
}
