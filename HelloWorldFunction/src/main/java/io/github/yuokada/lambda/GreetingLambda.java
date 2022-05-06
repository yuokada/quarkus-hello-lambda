package io.github.yuokada.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.github.yuokada.lambda.model.InputEvent;
import io.github.yuokada.lambda.model.OutputResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import java.util.Set;

public class GreetingLambda
        implements RequestHandler<InputEvent, OutputResponse>
{
    @Inject
    Validator validator;

    @Override
    public OutputResponse handleRequest(InputEvent input, Context context)
    {
        return handleRequestWithValidation(input, context);
    }

    public OutputResponse handleRequestWithValidation(@Valid InputEvent input, Context context)
    {
        Set<ConstraintViolation<InputEvent>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            return new OutputResponse().setMessage("InputEvent is invalid!");
        }

        if (input.getName() != null) {
            OutputResponse response = new OutputResponse().setName(input.getName()).setMessage("Hello " + input.getName());
            return response;
        }
        else {
            OutputResponse response = new OutputResponse().setMessage("Hello World");
            return response;
        }
    }
}
