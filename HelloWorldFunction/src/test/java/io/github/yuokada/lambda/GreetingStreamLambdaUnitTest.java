package io.github.yuokada.lambda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import jakarta.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GreetingStreamLambdaUnitTest {

    private GreetingStreamLambda handlerWith(Validator validator) {
        return new GreetingStreamLambda(ValidatorTestHelper.serviceWith(validator));
    }

    @Test
    void handleRequestWritesGreetingForValidInput() throws IOException {
        GreetingStreamLambda handler = handlerWith(ValidatorTestHelper.realValidator());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(
                new ByteArrayInputStream("{\"name\":\"Randy\"}".getBytes(StandardCharsets.UTF_8)),
                output,
                null);

        assertEquals(
                "{\"name\":\"Randy\",\"message\":\"Hello Randy\"}",
                output.toString(StandardCharsets.UTF_8));
    }

    @Test
    void handleRequestWritesHelloWorldWhenValidatorAllowsNullName() throws IOException {
        GreetingStreamLambda handler = handlerWith(ValidatorTestHelper.permissiveValidator());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(
                new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)), output, null);

        assertEquals(
                "{\"name\":null,\"message\":\"Hello World\"}",
                output.toString(StandardCharsets.UTF_8));
    }

    @Test
    void handleRequestWritesValidationMessageForUnknownField() throws IOException {
        GreetingStreamLambda handler = handlerWith(ValidatorTestHelper.realValidator());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(
                new ByteArrayInputStream(
                        "{\"unknown\":\"value\"}".getBytes(StandardCharsets.UTF_8)),
                output,
                null);

        assertTrue(output.toString(StandardCharsets.UTF_8).contains("InputEvent is invalid!"));
    }

    @Test
    void handleRequestWritesValidationMessageForBlankName() throws IOException {
        GreetingStreamLambda handler = handlerWith(ValidatorTestHelper.realValidator());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(
                new ByteArrayInputStream("{\"name\":\"\"}".getBytes(StandardCharsets.UTF_8)),
                output,
                null);

        assertTrue(output.toString(StandardCharsets.UTF_8).contains("InputEvent is invalid!"));
    }

    @Test
    void handleRequestSwallowsRuntimeExceptionFromMapper() throws IOException {
        GreetingStreamLambda handler = handlerWith(ValidatorTestHelper.permissiveValidator());
        handler.mapper =
                new ObjectMapper() {
                    @Override
                    public <T> T readValue(java.io.Reader src, Class<T> valueType) {
                        throw new RuntimeException("boom");
                    }
                };
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(
                new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)), output, null);

        assertEquals("", output.toString(StandardCharsets.UTF_8));
    }
}
