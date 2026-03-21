package io.github.yuokada.lambda.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LambdaModelTest {

    @Test
    void inputEventAccessorsAndToStringWork() {
        InputEvent event = new InputEvent();

        event.setName("Randy");

        assertEquals("Randy", event.getName());
        assertTrue(event.toString().contains("Randy"));
    }

    @Test
    void outputResponseFluentSettersWork() {
        OutputResponse response = new OutputResponse();

        OutputResponse chained = response.setName("Randy").setMessage("Hello Randy");

        assertSame(response, chained);
        assertEquals("Randy", response.getName());
        assertEquals("Hello Randy", response.getMessage());
    }
}
