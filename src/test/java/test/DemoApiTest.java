package test;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.concurrent.ExecutionException;

import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;

@QuarkusTest
public class DemoApiTest {
    @Inject
    @GraphQLClient("demo-api")
    DynamicGraphQLClient dynamicClient;

    @Test
    public void testDemoApi() {
        Document query = document(
            operation(
                "GetMessage",
                field("hello",
                    field("text"),
                    field("title")
                )
            )
        );
        try {
            Response response = dynamicClient.executeSync(query);
            Assertions.assertFalse(response.hasError());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
