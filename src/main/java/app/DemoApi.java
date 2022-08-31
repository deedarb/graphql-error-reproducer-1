package app;

import io.vertx.core.http.HttpServerRequest;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import java.util.Optional;

@GraphQLApi
public class DemoApi {
    @Query
    public Message getHello() {
        String lang = Optional.ofNullable(CDI.current())
            .map(cdi -> cdi.select(HttpServerRequest.class))
            .map(Instance::get)
            .flatMap(I18nLabel::extractLanguage).orElse("en");
        System.out.println("lang is extracted: " + lang);
        return new Message("hello world", I18nLabel.of("English", "Russian"));
    }


}
