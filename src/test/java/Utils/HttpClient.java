package Utils;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.Optional;

public class HttpClient {
    private final Configuration config = new Configuration();
    public ValidatableResponse post(File payload, String endpoint, String token) {
        baseURI = config.getBaseUrl();

        return  given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post(endpoint)
                .then();
    }

    public ValidatableResponse put(File payload, String endpoint, String token) {
        baseURI = config.getBaseUrl();

        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .put(endpoint)
                .then();
    }

    public ValidatableResponse delete(String endpoint, String token) {
        baseURI = config.getBaseUrl();

        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(endpoint)
                .then();
    }

    public ValidatableResponse get(String endpoint, String token) {
        baseURI = config.getBaseUrl();

        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(endpoint)
                .then();
    }
}
