package io.github.yuokada.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.github.yuokada.lambda.model.InputEvent;
import io.github.yuokada.lambda.model.OutputResponse;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.jboss.logging.Logger;

@Named("stream-handler")
public class GreetingStreamLambda implements RequestStreamHandler {

    private static final Logger logger = Logger.getLogger(GreetingStreamLambda.class);
    @Inject
    Validator validator;
    // Jackson
    ObjectMapper mapper = new ObjectMapper();

    public OutputResponse handleRequestWithValidation(@Valid InputEvent input, Context context) {
        Set<ConstraintViolation<InputEvent>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if (input.getName() != null) {
            OutputResponse response = new OutputResponse().setName(input.getName())
                    .setMessage("Hello " + input.getName());
            return response;
        } else {
            OutputResponse response = new OutputResponse().setMessage("Hello World");
            return response;
        }
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context)
            throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(output, StandardCharsets.UTF_8));

        try {
            InputEvent inputEvent = mapper.readValue(reader, InputEvent.class);
            logger.info(input);
            OutputResponse response = handleRequestWithValidation(inputEvent, context);
            writer.write(mapper.writeValueAsString(response));
        } catch (UnrecognizedPropertyException | ConstraintViolationException e) {
            String message = String.format("InputEvent is invalid! (%s)", e.getMessage());
            OutputResponse response = new OutputResponse().setMessage(message);
            writer.write(mapper.writeValueAsString(response));
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
        } finally {
            reader.close();
            writer.close();
        }
    }
}
