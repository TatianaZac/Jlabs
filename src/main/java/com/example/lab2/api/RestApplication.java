package com.example.lab2.api;

import com.example.lab2.api.resource.ApartmentResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Точка входу для REST API.
 * Усі REST-запити починаються з /api.
 */
@ApplicationPath("/api")
public class RestApplication extends ResourceConfig {

    public RestApplication() {
        register(ApartmentResource.class);
    }
}
