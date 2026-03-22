package io.github.yuokada.lambda;

import java.util.Set;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import io.github.yuokada.lambda.model.InputEvent;
import io.github.yuokada.lambda.model.OutputResponse;

@ApplicationScoped
public class GreetingService {

    private final Validator validator;

    GreetingService() {
        this(null); // CDI no-arg constructor for proxy subclass
    }

    @Inject
    public GreetingService(Validator validator) {
        this.validator = validator;
    }

    public OutputResponse buildResponse(InputEvent input) {
        Set<ConstraintViolation<InputEvent>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if (input.getName() != null) {
            return new OutputResponse()
                    .setName(input.getName())
                    .setMessage("Hello " + input.getName());
        }
        return new OutputResponse().setMessage("Hello World");
    }
}
