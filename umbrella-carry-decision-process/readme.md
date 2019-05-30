# Umbrella Carry Decision Process :umbrella:

A sample process using [Kogito JBPM and Drools Runtimes](https://github.com/kiegroup/kogito-runtimes) to help you decide if it's worth carry an umbrella with you today based on your location's weather forecast.

## How to use

1) Read the [Kogito examples](https://github.com/kiegroup/kogito-examples).

2) Run the following commands in your terminal to have the application started:

```shell
git clone https://github.com/ricardozanini/tenkichannel.git
cd umbrella-carry-decision-process
mvn clean package quarkus:dev -Dquarkus.http.port=8181
```

The `-Dquarkus.http.port=8181` option is optional, but will help if you are running the jBPM Designer on the the default `8080` port.

3) Make calls to the API to start the process like this:

```shell
curl -X --data "{'location': {'city' : 'Seattle,WA'}}" http://localhost:8181/umbrellaCarryDecisionProcess
```

4) You should receive a response like this one:

```shell
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

Yes, please bring an umbrella with you because it's gonna rain :umbrella:.