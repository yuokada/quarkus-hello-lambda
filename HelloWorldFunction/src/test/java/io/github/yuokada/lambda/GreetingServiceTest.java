package io.github.yuokada.lambda;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

import io.github.yuokada.lambda.model.InputEvent;
import io.github.yuokada.lambda.model.OutputResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GreetingServiceTest {

    @Test
    void buildResponseReturnsHelloWorldWhenValidatorAllowsNullName() {
        GreetingService service =
                ValidatorTestHelper.serviceWith(ValidatorTestHelper.permissiveValidator());

        OutputResponse response = service.buildResponse(new InputEvent());

        assertEquals("Hello World", response.getMessage());
    }

    @Test
    void buildResponseThrowsForInvalidInput() {
        GreetingService service =
                ValidatorTestHelper.serviceWith(ValidatorTestHelper.realValidator());
        InputEvent input = new InputEvent();
        input.setName("");

        assertThrows(ConstraintViolationException.class, () -> service.buildResponse(input));
    }
}
