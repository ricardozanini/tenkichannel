package org.tenkichannel.forecast.rain.http;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//see: https://github.com/quarkusio/quarkus/issues/3598

@Provider
public class CORSFilter implements ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CORSFilter.class);

    public CORSFilter() {}

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        LOGGER.debug("Modifing response with CORSFIlter: {}", responseContext.getHeaders());
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.putSingle("Access-Control-Allow-Origin", "*");
        LOGGER.debug("Modified to add the required header: {}", responseContext.getHeaders());
    }

}
