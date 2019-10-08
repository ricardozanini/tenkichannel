# Tenki (天気) Channel :cloud:

Tenki Channel is a tech demo that puts [Kogito](https://kogito.kie.org/), [Camel](https://kogito.kie.org/), [Quarkus](https://kogito.kie.org/) and OpenShift together to showcase some examples using those components.

Tenki (天気) in Japanese means _weather_. :-D

## Rain Forecast - Is it gonna rain? :umbrella:

In this use case we have two services working together to answer a simple question: "Is it gonna rain on my location?".

The first service, [**Rain Forecast Process**](rain-forecast-process) uses the process automation framework, Kogito, to automate a forecast process that will call the [**Weather API Gateway**](weather-api-gateway). Based on the weather forecast results, calculated by a decision rule, the process will give you the answer.

The Weather API Gateway is a Camel service that hides the external weather service and exposes the domain data that we're interested in. The gateway implements (in a certain level) the [API Gateway pattern](https://microservices.io/patterns/apigateway.html).

Both services are backed by the Quarkus framework. This means that you can run in native code in the cloud.

To make all this work, read the guides ([here](rain-forecast-process) and [here](weather-api-gateway)) and have both services up and running. You should call the Rain Forecast Process at the `rainforecast` endpoint to see it in action.

### Deploying on OpenShift ⭕️

You can either use templates or the [Kogito Operator](https://github.com/kiegroup/kogito-cloud-operator) to deploy the Rain Forecast Application. The first (and easier) method will take the pre built images from Quay and deploy them into your cluster.

The second approach, will build the application for you from this master branch. It's worth taking a look to get your hands dirty on the operator way of doing things on OpenShift.

#### Using Templates

It's pretty easy to deploy the Rain Forecast demo on your OpenShift cluster:

```bash
# clone this repo
git clone https://github.com/ricardozanini/tenkichannel.git
$ cd tenkichannel
$ oc create new-project tenkichannel
$ oc create -f openshift/rain-forecast/templates/rain-forecast-backend.yaml
# grab your API key on https://openweathermap.org/api
$ oc new-app --template=rain-forecast-demo -p NAMESPACE=tenkichannel -p OPENWEATHER_API_KEY=<your-api-key>
```

Then, use the route to access the process:

```bash
$ oc get route

NAME            HOST/PORT                                                           PATH   SERVICES        PORT       TERMINATION   WILDCARD
rain-forecast   rain-forecast-tenkichannel.apps.your-cluster.com                           rain-forecast   8080-tcp    edge         None
```

See how to use it at the [Rain Forecast README doc](rain-forecast-process/README.md).

#### Using the Kogito Operator/CLI

Deploying using those templates it's easy to get the application working right away since we used pre built images, but in the real world you'll have to build (no pun intended) your way in.

That's why we have the [Kogito Operator](https://github.com/kiegroup/kogito-cloud-operator) to do the hard job to you. First thing, [install it](https://github.com/kiegroup/kogito-cloud-operator#installation) in your cluster and then you can deploy the Rain Forecast application with a CR:

```bash
$ oc new-project tenkichannel
$ oc create -f openshift/rain-forecast/operator/rain-forecast-kogitoapp.yaml
```

If you're feeling lazy, let the [Kogito CLI](https://github.com/kiegroup/kogito-cloud-operator#kogito-cli) to deploy the operator (only on 0.5.0+ versions) and the application for you:

```bash
$ kogito new-project tenkichannel
$ kogito deploy rain-forecast https://github.com/ricardozanini/tenkichannel --context-dir=rain-forecast-process -e NAMESPACE=tenkichannel -p tenkichannel
```

Edit the deployed route to use TLS since we're going to need secure connections in our demo. You can get the route with:

```bash
$ oc describe kogitoapp/rain-forecast | grep Route:
  Route:  https://rain-forecast-tenkichannel.mycluster.com
```

The Weather API Gateway is not a Kogito Service, so you can't use the operator to deploy it. Instead, use the `new-app` command from `oc` client:

```bash
$ oc new-app https://github.com/ricardozanini/tenkichannel --name=weather-api-gateway --context-dir=weather-api-gateway -e JAVA_OPTIONS="-Dorg.tenkichannel.weather.api.gateway.openweathermap.api_key=<your_api_key>" --docker-image=docker.io/fabric8/s2i-java:latest-java11 -l forecast=service
```

#### Deploying the User Interface

No matter the way you decided to deploy the Rain Forecast application, you can deploy the UI to have the full demo experience.

After having the backend working and playing with the API, it's time to have some fun with the UI. To deploy it, you'll need the Rain Forecast Process route url to make this work. Given that you've already deployed the `rain-forecast` application like stated above, just do:

```bash
# create the UI template
$ oc create -f openshift/rain-forecast/templates/rain-forecast-ui.yaml
# create the UI application using as parameter the rain-forecast route that were generated in the above section
$ oc new-app --template=rain-forecast-demo-ui -p BACKEND_ROUTE=https://rain-forecast-tenkichannel.apps.your-cluster.com
```

You should see the build spinning and in a couple minutes your application will be ready for use.

If you're running on a development environment, chances are that you're using self signed certificates. If this is the case, try to access the backend route at least once to have your browser to trust in your certificate.

Access the application using the new route, allow the browser to read your location and have fun!

![](docs/img/rain-forecast-ui-ss.png)

## Contributing

This is a work in progress and mainly used for presentations and general demos. If you see something wrong, please don't hesitate and send us a PR or file a issue in this repo. :)
