package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.CloudEventsFunction;
import com.google.events.cloud.pubsub.v1.MessagePublishedData;
import io.cloudevents.CloudEvent;
import java.util.Base64;
import java.util.logging.Logger;

public class SourceFunction implements CloudEventsFunction {
    private static final Logger logger = Logger.getLogger(SourceFunction.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void accept(CloudEvent cloudEvent) throws Exception {


        logger.info("This is message from source cloud function: " + new String(cloudEvent.getData().toBytes()));
    }
}
