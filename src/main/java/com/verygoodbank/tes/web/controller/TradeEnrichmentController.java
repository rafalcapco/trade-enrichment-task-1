package com.verygoodbank.tes.web.controller;


import com.verygoodbank.tes.web.service.TradeDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1")
public class TradeEnrichmentController {

    private static final Logger LOG = LoggerFactory.getLogger(TradeEnrichmentController.class);
    final TradeDataService tradeDataService;

    public TradeEnrichmentController(TradeDataService tradeDataService) {
        this.tradeDataService = tradeDataService;
    }

    /**
     * Endpoint used to enrich trade data.
     * @param file - file to enrich
     * @return enriched file
     */
    @PostMapping(value = "/enrich", produces = {MediaType.MULTIPART_FORM_DATA_VALUE}, consumes = {
        MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<InputStreamResource> enrich(@RequestPart("file") MultipartFile file) {
        LOG.info("Received enrich request with file: {}", file.getResource().getFilename());

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header("Content-Disposition", "attachment; filename=\"" + "enriched" + file.getName() + "\"")
            .body(tradeDataService.enrichData(file));
    }
}


