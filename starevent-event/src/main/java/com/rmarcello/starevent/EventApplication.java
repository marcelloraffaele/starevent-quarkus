package com.rmarcello.starevent;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.ExternalDocumentation;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@ApplicationPath("/")
@OpenAPIDefinition(info = @Info(title = "Event API", 
    description = "This API allows operations on starevent events", version = "1.0", 
    contact = @Contact(name = "@rmarcello", url = "https://twitter.com/rmarcello")), 
    externalDocs = @ExternalDocumentation(url = "https://https://marcelloraffaele.github.io", 
    description = "marcelloraffaele personal blog"), 
    tags = {
        @Tag(name = "api", description = "API used for starevent application"),
        @Tag(name = "event", description = "starevent events management") })
public class EventApplication extends Application{
    
}
