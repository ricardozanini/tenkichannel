# Rain Forecast Process :umbrella:

A sample process using [Kogito Runtimes](https://github.com/kiegroup/kogito-runtimes) to help you decide if it's worth carrying an umbrella based on your location's weather forecast.

## How to use

1. Read the [Kogito examples](https://github.com/kiegroup/kogito-examples) to get used with the Kogito project.

2. Have the [Weather API Gateway](../weather-api-gateway) ready on port `8081` 

3. Run the following commands in your terminal to have the application started:

```shell
# have you cloned this repo? git clone https://github.com/ricardozanini/tenkichannel.git
cd tenkichannel/rain-forecast-process
mvn clean package quarkus:dev -Dlocal=true -DskipTests
```

Once the application is started, make calls to the API to start the process:

```shell
curl -X POST -H "Content-Type: application/json" --data '{"location": {"city" : "Seattle"}}' http://localhost:8080/rainforecast
```

You should receive a response like this one:

```json
{
    "id": 1,
    "location": {
        "city": "Seattle",
        "countryCode": "US"
    },
    "result": {
        "rain": true
    }
}
```

And if you live in [Seattle](https://en.wikipedia.org/wiki/Seattle), bring an umbrella with you because it's gonna rain :umbrella:.
