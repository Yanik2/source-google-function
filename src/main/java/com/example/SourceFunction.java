package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.CloudEventsFunction;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import io.cloudevents.CloudEvent;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceFunction implements CloudEventsFunction {
    private static final Logger logger = LoggerFactory.getLogger(SourceFunction.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Storage storage = StorageOptions.getDefaultInstance().getService();

    @Override
    public void accept(CloudEvent cloudEvent) throws Exception {
        logger.info("Received event");
        final var jsonNode = objectMapper.readTree(cloudEvent.getData().toBytes());
        final var encodedData = jsonNode.get("data").asText();
        final var decodedData = Base64.getDecoder().decode(encodedData);
        final var decodedJson = objectMapper.readTree(decodedData);
        final var filename = getFileName(decodedJson);
        final var bais = new ByteArrayInputStream(decodedData);
        storage.createFrom(
            BlobInfo.newBuilder(
                    BucketInfo.newBuilder("yz-test-bucket").build(), filename)
                .build(),
            bais);
        logger.info("File saved: " + filename);
    }


    private String getFileName(JsonNode jsonNode) {
        return switch (jsonNode.get("businessProcess").asText()) {
            case "USER" -> "auditlogs/user";
            case "INVENTORY" -> "auditlogs/inventory";
            default -> "auditlogs/clinical-trial/" + jsonNode.get("clinicalTrialId").asText();
        };
    }
}
