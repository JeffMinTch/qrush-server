package com.app.qrush.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private String baseUri;

    private String eventUri;

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getEventUri() {
        return eventUri;
    }

    public void setEventUri(String eventUri) {
        this.eventUri = eventUri;
    }
}
