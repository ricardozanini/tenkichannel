package org.tenkichannel.weather.api.gateway.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.camel.ProducerTemplate;
import org.tenkichannel.weather.api.gateway.domain.Location;
import org.tenkichannel.weather.api.gateway.domain.Weather;
import org.tenkichannel.weather.api.gateway.routes.openweathermap.OpenWeatherMapRouteFactory;

@Path("/api/weather")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WeatherResource {

    @Inject
    ProducerTemplate producer;

    @GET
    @Path("/city/{id}")
    public Weather getWeatherCondition(@PathParam("id") int cityId) {
        return producer.requestBody(OpenWeatherMapRouteFactory.ROUTE_CURRENT_WEATHER_DATA, new Location(cityId), Weather.class);
    }

    @GET
    @Path("/location/{city}")
    public Weather getWeatherCondition(@PathParam("city") String cityName) {
        return producer.requestBody(OpenWeatherMapRouteFactory.ROUTE_CURRENT_WEATHER_DATA, new Location(cityName), Weather.class);
    }

    @GET
    @Path("/geo/{lat}/{log}")
    public Weather getWeatherCondition(@PathParam("lat") Double latitude, @PathParam("log") Double longitude) {
        return producer.requestBody(OpenWeatherMapRouteFactory.ROUTE_CURRENT_WEATHER_DATA, new Location(latitude, longitude), Weather.class);
    }

}
