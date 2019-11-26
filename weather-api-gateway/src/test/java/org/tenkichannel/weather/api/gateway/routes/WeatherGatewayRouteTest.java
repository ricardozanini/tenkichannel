package org.tenkichannel.weather.api.gateway.openweather.openweathermap;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.tenkichannel.weather.api.gateway.openweather.OpenWeatherDataProperties;
import org.tenkichannel.weather.api.gateway.yahooweather.YahooWeatherDataProperties;

import java.util.Scanner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class WeatherGatewayRouteTest {

    @BeforeAll
    public static void setup() {
        RestAssured.basePath = "/";
    }

    @AfterAll
    public static void cleanup() {
        System.clearProperty(OpenWeatherDataProperties.OPEN_WEATHER_MAP_BASE_URI);
        System.clearProperty(YahooWeatherDataProperties.YAHOO_WEATHER_BASE_URI);
    }

    @Test
    public void whenQueryWeatherByCityId() throws Exception {
        try (final MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody(readResource("/mock-responses/openweathermap/current.json")));
            server.enqueue(new MockResponse().setBody(readResource("/mock-responses/yahooweather/weather_ydn_js.json")));
            server.start(9090);

            // first call to OpenWeather
            given()
                    .when().get("/api/weather/city/2643743")
                    .then()
                    .statusCode(200)
                    .body(containsString("Clear"));

            // second call to YahooWeather
            given()
                    .when().get("/api/weather/city/2643743")
                    .then()
                    .statusCode(200)
                    .body(containsString("Scattered Showers"));
        }
    }

    private static String readResource(final String path) {
        try (Scanner s = new Scanner(WeatherGatewayRouteTest.class.getResourceAsStream(path))) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }

}
