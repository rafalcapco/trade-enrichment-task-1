package com.verygoodbank.tes.web.service;

import com.verygoodbank.tes.web.config.ProductCatalog;
import com.verygoodbank.tes.web.exception.InvalidFileException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TradeDataService {

    private static final Logger LOG = LoggerFactory.getLogger(TradeDataService.class);
    private static final String MISSING_PRODUCT_NAME = "Missing Product Name";
    private static final String COMMA_DELIMITER = ",";
    private static final int DATE_COLUMN_ID = 0;
    private static final int PRODUCT_COLUMN_ID = 1;
    private final ProductCatalog productCatalog;

    public TradeDataService(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    /**
     * Enriches given file using BufferedReader and BufferedWriter.
     * Operation of reading and writing is done simultaneously.
     * Invalid rows are discarded and product_id is replaced with product_name.
     * @param file - file to enrich
     * @return InputStreamResource which contains enriched data
     */
    public InputStreamResource enrichData(MultipartFile file) {
        LOG.info("Attempting to enrich data from {} file", file.getResource().getFilename());
        FileInputStream fileInputStream = getEnrichedData(file);
        LOG.info("Successfully enriched data from {} file", file.getResource().getFilename());
        return new InputStreamResource(fileInputStream);
    }

    private FileInputStream getEnrichedData(MultipartFile file) {
        FileInputStream fileInputStream;
        try {
            File tmpFile = tryFileEnrichment(file);
            fileInputStream = new FileInputStream(tmpFile);
        } catch (IOException e) {
            LOG.error("There were problems processing the file {}. Enrichment interrupted.",
                file.getResource().getFilename(), e);
            throw new InvalidFileException(
                String.format("There were problems processing the file %s. Enrichment interrupted.",
                    file.getResource().getFilename()), e);
        }
        return fileInputStream;
    }

    private File tryFileEnrichment(MultipartFile file) throws IOException {
        File tmpFile = File.createTempFile("tmpFile", ".tmp");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile))) {
            lineEnrichment(reader, writer);
        }
        return tmpFile;
    }

    private void lineEnrichment(BufferedReader reader, BufferedWriter writer) throws IOException {
        String line;
        addHeaderLine(reader, writer);
        while ((line = reader.readLine()) != null) {
            List<String> lineAsList = Arrays.asList(line.split(COMMA_DELIMITER));
            if (isDateValid(lineAsList.get(DATE_COLUMN_ID))) {
                writer.write(modifyLine(lineAsList));
                writer.newLine();
            } else {
                LOG.warn("Invalid date in record: {}", line);
            }
        }
    }

    private void addHeaderLine(BufferedReader reader, BufferedWriter writer) throws IOException {
        String line;
        if ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.newLine();
        }
    }

    private String modifyLine(List<String> line) {
        String productId = line.get(PRODUCT_COLUMN_ID);
        String productName = productCatalog.getProducts().get(productId);
        if (productName == null) {
            LOG.warn("Missing mapping for id: {}", productId);
            productName = MISSING_PRODUCT_NAME;
        }
        line.set(PRODUCT_COLUMN_ID, productName);
        return String.join(",", line);
    }

    private boolean isDateValid(String date) {
        try {
            DateTimeFormatter.BASIC_ISO_DATE.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
