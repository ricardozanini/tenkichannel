package org.tenkichannel.weather.api.gateway.openweather.openweathermap;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.tenkichannel.weather.api.gateway.openweather.OpenWeatherDataProperties;

import java.util.Scanner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class OpenWeatherMapRouteTest {

    @BeforeAll
    public static void setup() {
        RestAssured.basePath = "/";
    }

    @AfterAll
    public static void cleanup() {
        System.clearProperty(OpenWeatherDataProperties.OPEN_WEATHER_MAP_BASE_URI);
    }

    @Test
    public void whenQueryWeatherByCityId() throws Exception {
        try (final MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody(readResource("/mock-responses/openweathermap/current.json")));
            server.start(9090);
            given()
                    .when().get("/api/weather/city/2643743")
                    .then()
                    .statusCode(200)
                    .body(containsString("Clear"));
        }
    }

    private static String readResource(final String path) {
        try (Scanner s = new Scanner(OpenWeatherMapRouteTest.class.getResourceAsStream(path))) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }

}
