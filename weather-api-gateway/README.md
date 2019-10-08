# Weather API Gateway

## Usage

Get an API Key at the [OpenWeather API web site](https://openweathermap.org/api) and run:

```bash
# don't forget to clone me! git clone https://github.com/ricardozanini/tenkichannel.git
$ cd tenkichannel/weather-api-gateway
# run the application
$ mvn clean package quarkus:dev -Dquarkus.http.port=8081 -Dorg.tenkichannel.weather.api.gateway.openweathermap.api_key=<my api key> -DskipTests
```

Enjoy the app at: [http://localhost:8080/swagger-ui/](http://localhost:8081/swagger-ui/).