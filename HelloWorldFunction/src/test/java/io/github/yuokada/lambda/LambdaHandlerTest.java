package io.github.yuokada.lambda;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import io.github.yuokada.lambda.model.InputEvent;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "GITHUB_ACTIONS", matches = "true")
public class LambdaHandlerTest {

    @Test
    public void testSimpleLambdaSuccess() throws Exception {
        // you test your lambas by invoking on http://localhost:8081
        // this works in dev mode too

        InputEvent in = new InputEvent();
        in.setName("Randy");
        given()
            .contentType("application/json")
            .accept("application/json")
            .body(in)
            .when()
            .post()
            .then()
            .statusCode(200)
            .body(containsString("{\"name\":\"Randy\",\"message\":\"Hello Randy\"}"));
    }

    @Test
    public void testSimpleLambdaWithoutParamSuccess() throws Exception {
        InputEvent in = new InputEvent();
        given()
            .contentType("application/json")
            .accept("application/json")
            .body(in)
            .when()
            .post()
            .then()
            .statusCode(200)
            .body(containsString("InputEvent is invalid!"));
    }


}
