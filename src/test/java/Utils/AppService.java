package Utils;
import java.io.File;
import java.io.IOException;

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

    public String getProposalTitle(String userId, String token) {
        return httpClient.get("/password/user/" + userId, token)
                .statusCode(200)
                .extract().path("data[0].title");
    }
}
