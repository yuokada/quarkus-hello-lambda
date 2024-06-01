package io.github.yuokada.lambda.model;

import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public class InputEvent
{
    @NotBlank(message = "Name may not be blank")
    String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "InputEvent{" +
                "name='" + name + '\'' +
                '}';
    }
}
