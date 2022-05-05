package io.github.yuokada.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.github.yuokada.lambda.model.InputEvent;
import io.github.yuokada.lambda.model.OutputResponse;

public class GreetingLambda
        implements RequestHandler<InputEvent, OutputResponse>
{

    @Override
    public OutputResponse handleRequest(InputEvent input, Context context)
    {
        if (input.getName().isPresent()) {
            OutputResponse response = new OutputResponse().setName(input.getName().get()).setMessage("Hello " + input.getName().get());
            return response;
        }
        else {
            OutputResponse response = new OutputResponse().setMessage("Hello World");
            return response;
        }
    }
}
