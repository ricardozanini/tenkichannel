# Umbrella Carry Decision Process :umbrella:

A sample process using [Kogito JBPM and Drools Runtimes](https://github.com/kiegroup/kogito-runtimes) to help you decide if it's worth carrying an umbrella based on your location's weather forecast.

## How to use

Read the [Kogito examples](https://github.com/kiegroup/kogito-examples).

Run the following commands in your terminal to have the application started:

```shell
git clone https://github.com/ricardozanini/tenkichannel.git
cd umbrella-carry-decision-process
mvn clean package quarkus:dev -Dquarkus.http.port=8181
```

The `-Dquarkus.http.port=8181` option is optional, but will help if you are running the jBPM Designer on the the default `8080` port.

Make calls to the API to start the process:

```shell
curl -X POST -H "Content-Type: application/json" --data '{"location": {"city" : "Seattle,WA"}}' http://localhost:8181/umbrellaCarryDecisionProcess
```

You should receive a response like this one:

```json
{
    "id": 1,
    "location": {
        "city": "Seattle,WA"
    },
    "result": {
        "carryUmbrella": true
    },
    "weather": {
        "condition": "Rain"
    }
}
```
And if you live in [Seattle](https://en.wikipedia.org/wiki/Seattle), bring an umbrella with you because it's gonna rain :umbrella:.
