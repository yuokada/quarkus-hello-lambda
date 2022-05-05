package io.github.yuokada.lambda.model;

import java.util.Optional;

public class OutputResponse
{
    Optional<String> name = Optional.empty();
    String message;

    public Optional<String> getName()
    {
        return name;
    }

    public OutputResponse setName(String name)
    {
        this.name = Optional.of(name);
        return this;
    }

    public String getMessage()
    {
        return message;
    }

    public OutputResponse setMessage(String message)
    {
        this.message = message;
        return this;
    }
}
