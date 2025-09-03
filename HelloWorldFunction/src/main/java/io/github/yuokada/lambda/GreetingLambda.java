package io.github.yuokada.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.github.yuokada.lambda.model.InputEvent;
import io.github.yuokada.lambda.model.OutputResponse;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

public class GreetingLambda implements RequestHandler<InputEvent, OutputResponse> {

  @Inject Validator validator;

  @Override
  public OutputResponse handleRequest(InputEvent input, Context context) {
    try {
      return handleRequestWithValidation(input, context);
    } catch (ConstraintViolationException e) {
      return new OutputResponse().setMessage("InputEvent is invalid!");
    }
  }

  public OutputResponse handleRequestWithValidation(@Valid InputEvent input, Context context) {
    if (input.getName() != null) {
      OutputResponse response =
          new OutputResponse().setName(input.getName()).setMessage("Hello " + input.getName());
      return response;
    } else {
      OutputResponse response = new OutputResponse().setMessage("Hello World");
      return response;
    }
  }
}
