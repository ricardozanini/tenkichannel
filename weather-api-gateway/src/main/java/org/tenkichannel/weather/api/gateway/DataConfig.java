package org.tenkichannel.weather.api.gateway;

/**
 * Base interface for defining how to config external weather APIs
 */
public interface DataConfig {
    boolean isSecureProtocol();

    String getBaseUri();
}
