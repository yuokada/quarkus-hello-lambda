package io.github.yuokada.lambda;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import org.junit.jupiter.api.Test;

import io.github.yuokada.lambda.model.InputEvent;
import io.github.yuokada.lambda.model.OutputResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GreetingLambdaUnitTest {

    private GreetingLambda handlerWith(Validator validator) {
        return new GreetingLambda(ValidatorTestHelper.serviceWith(validator));
    }

    @Test
    void handleRequestReturnsGreetingWhenNameIsPresent() {
        GreetingLambda handler = handlerWith(ValidatorTestHelper.realValidator());
        InputEvent input = new InputEvent();
        input.setName("Randy");

        OutputResponse response = handler.handleRequest(input, null);

        assertEquals("Randy", response.getName());
        assertEquals("Hello Randy", response.getMessage());
    }

    @Test
    void handleRequestReturnsValidationMessageForBlankName() {
        GreetingLambda handler = handlerWith(ValidatorTestHelper.realValidator());
        InputEvent input = new InputEvent();
        input.setName(" ");

        OutputResponse response = handler.handleRequest(input, null);

        assertEquals("InputEvent is invalid!", response.getMessage());
    }

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
