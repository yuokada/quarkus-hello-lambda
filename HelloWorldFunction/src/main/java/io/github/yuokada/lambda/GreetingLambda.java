package io.github.yuokada.lambda;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import io.github.yuokada.lambda.model.InputEvent;
import io.github.yuokada.lambda.model.OutputResponse;

public class GreetingLambda implements RequestHandler<InputEvent, OutputResponse> {

    private final GreetingService greetingService;

    @Inject
    public GreetingLambda(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Override
    public OutputResponse handleRequest(InputEvent input, Context context) {
        try {
            return greetingService.buildResponse(input);
        } catch (ConstraintViolationException e) {
            return new OutputResponse().setMessage("InputEvent is invalid!");
        }
    }
}
