package com.example;

import com.google.cloud.functions.CloudEventsFunction;
import io.cloudevents.CloudEvent;
import java.util.logging.Logger;

public class SourceFunction implements CloudEventsFunction {
    private static final Logger logger = Logger.getLogger(SourceFunction.class.getName());

    @Override
    public void accept(CloudEvent cloudEvent) throws Exception {
        logger.info("This is message from source cloud function");
    }
}
