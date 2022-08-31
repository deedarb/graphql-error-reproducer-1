package app;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

@GraphQLApi
public class DemoApi {
    @Query
    public Message getHello() {
        return new Message("hello world", I18nLabel.of("English", "Russian"));
    }
}
