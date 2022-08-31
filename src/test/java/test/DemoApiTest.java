package test;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@QuarkusTest
public class DemoApiTest {
    @Inject
    Vertx vertx;

    @Test
    public void testDemoApi() {
        WebClient webClient = WebClient.create(vertx);
        var queryStr = """
            query GetMessage {
              hello {
                text
              }
            }
            """;
        var failingQueryStr = """
            query GetMessage {
              hello {
                text
                title
              }
            }
            """;
        var query = new JsonObject(Map.of("query", failingQueryStr, "operationName", "GetMessage"));
        try {
            HttpResponse<JsonObject> response = webClient.postAbs("http://localhost:8080/graphql")
                .putHeader(HttpHeaders.ACCEPT_LANGUAGE.toString(), "en")
                .as(BodyCodec.jsonObject())
                .sendJsonObject(query)
                .toCompletionStage().toCompletableFuture().get();
            System.out.println("status code: " + response.statusCode());
            System.out.println("body: " + response.body().encodePrettily());
            Assertions.assertEquals(200, response.statusCode());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
