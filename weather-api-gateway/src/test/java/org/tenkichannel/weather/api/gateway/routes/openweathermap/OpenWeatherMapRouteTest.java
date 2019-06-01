package org.tenkichannel.weather.api.gateway.routes.openweathermap;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class OpenWeatherMapRouteTest {

    @BeforeAll
    public static void setup() {
        RestAssured.basePath = "/api/weather";
    }

    @Test
    public void whenQueryWeatherByCityName() {
        given()
               .when().get("/city/2643743")
               .then()
               .statusCode(200)
               .body(containsString("condition"));
    }

}
