package com.verygoodbank.tes.web.config;

import com.verygoodbank.tes.web.util.CsvConverter;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ProductCatalog {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCatalog.class);

    private Map<String, String> products;

    @PostConstruct
    public void init() throws IOException {
        LOG.info("Attempting to initialize ProductCatalog");
        File resource = new ClassPathResource(
            "product.csv").getFile();
        List<List<String>> productsFromResource = CsvConverter.readCsv(resource);
        products = productsFromResource.stream()
            .skip(1)
            .collect(Collectors.toMap(
                product -> product.get(0),
                product -> product.get(1)));
        LOG.info("Successfully initialized ProductCatalog");
    }

    public Map<String, String> getProducts() {
        return products;
    }
}
