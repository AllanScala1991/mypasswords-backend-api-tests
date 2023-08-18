package Utils;

import java.io.File;
import java.io.IOException;

public class AppService {
    private final JsonService jsonService = new JsonService();
    private final HttpClient httpClient = new HttpClient();
    public String createUser(String name, String email, String username) throws IOException {
        jsonService.createUser(name, email, username);
        File payload = new File("src/test/resources/payloads/user/createUser.json");
        return httpClient.post(payload, "/user")
                .statusCode(201)
                .extract().path("data.id");
    }
}
