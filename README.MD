# Tenki (天気) Channel :cloud:

Tenki Channel is a tech demo that puts [Kogito](https://kogito.kie.org/), [Camel](https://kogito.kie.org/), [Quarkus](https://kogito.kie.org/) and OpenShift together to showcase some examples using those components.

Tenki (天気) in Japanese means _weather_. :-D

## Rain Forecast - Is it gonna rain? :umbrella:

In this use case we have two services working together to answer a simple question: "Is it gonna rain on my location?".

The first service, [**Rain Forecast Process**](rain-forecast-process) uses the process automation framework, Kogito, to automate a forecast process that will call the [**Weather API Gateway**](weather-api-gateway). Based on the weather forecast results, calculated by a decision rule, the process will give you the answer.

The Weather API Gateway is a Camel service that hides the an external weather service and exposes the domain data that we're interested into. The gateway implements (in a certain level) the API Gateway pattern.

Both services are backed by the Quarkus framework. This means that you can run in native code in the cloud.

To make all this work, read the guides ([here](rain-forecast-process) and [here](weather-api-gateway)) and have both services up and running. You should call the Rain Forecast Process at the `rainforecast` endpoint to see it in action.

## TODO

* [ ] OpenShift/Kubernetes templates to deploy the services into the cloud 
* [ ] Deploy the Kogito service via Kogito CLI 
