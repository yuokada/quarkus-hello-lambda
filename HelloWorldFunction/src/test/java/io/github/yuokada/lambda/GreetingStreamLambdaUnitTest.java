package io.github.yuokada.lambda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GreetingStreamLambdaUnitTest {

    @Test
    void handleRequestWritesGreetingForValidInput() throws IOException {
        GreetingStreamLambda handler = new GreetingStreamLambda();
        handler.validator = Validation.buildDefaultValidatorFactory().getValidator();
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
        GreetingStreamLambda handler = new GreetingStreamLambda();
        handler.validator = permissiveValidator();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(
                new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)), output, null);

        assertEquals(
                "{\"name\":null,\"message\":\"Hello World\"}",
                output.toString(StandardCharsets.UTF_8));
    }

    @Test
    void handleRequestWritesValidationMessageForUnknownField() throws IOException {
        GreetingStreamLambda handler = new GreetingStreamLambda();
        handler.validator = Validation.buildDefaultValidatorFactory().getValidator();
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
        GreetingStreamLambda handler = new GreetingStreamLambda();
        handler.validator = Validation.buildDefaultValidatorFactory().getValidator();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(
                new ByteArrayInputStream("{\"name\":\"\"}".getBytes(StandardCharsets.UTF_8)),
                output,
                null);

        assertTrue(output.toString(StandardCharsets.UTF_8).contains("InputEvent is invalid!"));
    }

    @Test
    void handleRequestSwallowsRuntimeExceptionFromMapper() throws IOException {
        GreetingStreamLambda handler = new GreetingStreamLambda();
        handler.validator = permissiveValidator();
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

    private Validator permissiveValidator() {
        return (Validator)
                Proxy.newProxyInstance(
                        Validator.class.getClassLoader(),
                        new Class<?>[] {Validator.class},
                        (proxy, method, args) -> {
                            if ("validate".equals(method.getName())) {
                                return Collections.<ConstraintViolation<Object>>emptySet();
                            }
                            if ("forExecutables".equals(method.getName())) {
                                throw new UnsupportedOperationException();
                            }
                            if ("unwrap".equals(method.getName())) {
                                throw new UnsupportedOperationException();
                            }
                            return defaultValue(method.getReturnType());
                        });
    }

    private Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (boolean.class.equals(type)) {
            return false;
        }
        if (char.class.equals(type)) {
            return '\0';
        }
        if (byte.class.equals(type)
                || short.class.equals(type)
                || int.class.equals(type)
                || long.class.equals(type)) {
            return 0;
        }
        if (float.class.equals(type) || double.class.equals(type)) {
            return 0.0;
        }
        return null;
    }
}
