# Weather API Gateway

## Usage

1. Get an API Key at the [OpenWeather API web site](https://openweathermap.org/api)
2. Get an Yahoo credential from [Yahoo Weather API](https://developer.yahoo.com/weather/documentation.html)
3. Run:

```bash
# don't forget to clone me! git clone https://github.com/ricardozanini/tenkichannel.git
$ cd tenkichannel/weather-api-gateway
# run the application
$ mvn clean package quarkus:dev -Dquarkus.http.port=8081 -Dorg.tenkichannel.weather.api.gateway.openweathermap.api_key=<my api key>  -Dorg.tenkichannel.weather.api.gateway.yahooweather.id=<yahoo_api_id> -Dorg.tenkichannel.weather.api.gateway.yahooweather.consumerKey=<yahoo_consumer_key> -Dorg.tenkichannel.weather.api.gateway.yahooweather.consumerSecret=<yahoo_consume_secret>
-DskipTests
```

Enjoy the app at: [http://localhost:8080/swagger-ui/](http://localhost:8081/swagger-ui/).