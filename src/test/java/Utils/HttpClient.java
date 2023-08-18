package Utils;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.*;

import java.io.File;

public class HttpClient {
    private final Configuration config = new Configuration();
    public ValidatableResponse post(File payload, String endpoint) {
        baseURI = config.getBaseUrl();

        return  given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(endpoint)
                .then();
    }
}
