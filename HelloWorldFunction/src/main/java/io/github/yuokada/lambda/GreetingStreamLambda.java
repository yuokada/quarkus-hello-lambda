package io.github.yuokada.lambda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolationException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.jboss.logging.Logger;

import io.github.yuokada.lambda.model.InputEvent;
import io.github.yuokada.lambda.model.OutputResponse;

@Named("stream-handler")
public class GreetingStreamLambda implements RequestStreamHandler {

    private static final Logger logger = Logger.getLogger(GreetingStreamLambda.class);
    private final GreetingService greetingService;
    ObjectMapper mapper;

    @Inject
    public GreetingStreamLambda(GreetingService greetingService) {
        this.greetingService = greetingService;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context)
            throws IOException {
        try (BufferedReader reader =
                        new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
                BufferedWriter writer =
                        new BufferedWriter(
                                new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
            try {
                InputEvent inputEvent = mapper.readValue(reader, InputEvent.class);
                logger.info(inputEvent);
                writer.write(mapper.writeValueAsString(greetingService.buildResponse(inputEvent)));
            } catch (UnrecognizedPropertyException | ConstraintViolationException e) {
                String message = String.format("InputEvent is invalid! (%s)", e.getMessage());
                writer.write(
                        mapper.writeValueAsString(new OutputResponse().setMessage(message)));
            } catch (RuntimeException e) {
                logger.error(e);
            }
        }
    }
}
