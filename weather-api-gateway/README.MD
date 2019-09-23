# Weather API Gateway

## Usage

Get an API Key at the [OpenWeather API web site](https://openweathermap.org/api) and run:

```bash
# clone it
$ git clone https://github.com/ricardozanini/tenkichannel.git
$ cd tenkichannel
# install all the bits
$ mvn clean install
# run the application
$ cd weather-api-gateway
$ mvn clean package quarkus:dev -Dorg.tenkichannel.weather.api.gateway.openweathermap.api_key=<my api key> -DskipTests
```

Enjoy the app at: [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/).