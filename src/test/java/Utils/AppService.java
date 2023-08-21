package Utils;

import io.restassured.response.ResponseBodyExtractionOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class AppService {
    private final JsonService jsonService = new JsonService();
    private final HttpClient httpClient = new HttpClient();
    public String createUser(String name, String email, String username) throws IOException {
        jsonService.createUser(name, email, username);
        File payload = new File("src/test/resources/payloads/user/createUser.json");
        return httpClient.post(payload, "/user", "")
                .statusCode(201)
                .extract().path("data.id");
    }

    public String getToken(String username, String password) throws IOException {
        jsonService.login(username, password);
        File payload = new File("src/test/resources/payloads/login/login.json");
        return httpClient.post(payload, "/login", "")
                .statusCode(200)
                .extract().path("data.token");
    }

    public void createPassword(String userId, String token) throws IOException {
        jsonService.createPassword(userId);
        File payload = new File("src/test/resources/payloads/password/CreatePassword.json");
        httpClient.post(payload, "/password", token)
                .statusCode(201);
    }

    public ResponseBodyExtractionOptions getPasswordByUserId(String userId, String token) {
        return httpClient.get("/password/user/" + userId, token)
                .log().all()
                .statusCode(200)
                .extract().path("data[0]");
    }
}
