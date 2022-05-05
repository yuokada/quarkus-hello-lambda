package io.github.yuokada.lambda.model;

import java.util.Optional;

public class InputEvent
{
    Optional<String> name = Optional.empty();

    public Optional<String> getName()
    {
        return name;
    }

    public void setName(Optional<String> name)
    {
        this.name = name;
    }
}
