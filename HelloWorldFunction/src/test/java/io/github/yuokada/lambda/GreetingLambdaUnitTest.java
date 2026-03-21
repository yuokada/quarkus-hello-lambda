package io.github.yuokada.lambda;

import java.lang.reflect.Proxy;
import java.util.Collections;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.Test;

import io.github.yuokada.lambda.model.InputEvent;
import io.github.yuokada.lambda.model.OutputResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GreetingLambdaUnitTest {

    @Test
    void handleRequestReturnsGreetingWhenNameIsPresent() {
        GreetingLambda handler = new GreetingLambda();
        handler.validator = validator();
        InputEvent input = new InputEvent();
        input.setName("Randy");

        OutputResponse response = handler.handleRequest(input, null);

        assertEquals("Randy", response.getName());
        assertEquals("Hello Randy", response.getMessage());
    }

    @Test
    void handleRequestReturnsValidationMessageForBlankName() {
        GreetingLambda handler = new GreetingLambda();
        handler.validator = validator();
        InputEvent input = new InputEvent();
        input.setName(" ");

        OutputResponse response = handler.handleRequest(input, null);

        assertEquals("InputEvent is invalid!", response.getMessage());
    }

    @Test
    void handleRequestWithValidationReturnsHelloWorldWhenValidatorAllowsNullName() {
        GreetingLambda handler = new GreetingLambda();
        handler.validator = permissiveValidator();

        OutputResponse response = handler.handleRequestWithValidation(new InputEvent(), null);

        assertEquals("Hello World", response.getMessage());
    }

    @Test
    void handleRequestWithValidationThrowsForInvalidInput() {
        GreetingLambda handler = new GreetingLambda();
        handler.validator = validator();
        InputEvent input = new InputEvent();
        input.setName("");

        assertThrows(
                ConstraintViolationException.class,
                () -> handler.handleRequestWithValidation(input, null));
    }

    private Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
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
