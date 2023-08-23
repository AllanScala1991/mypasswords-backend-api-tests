package com.mypasswords.password;

import Utils.AppService;
import Utils.FakerService;
import Utils.HttpClient;
import Utils.JsonService;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import java.io.File;
import java.io.IOException;

public class ShowPassword {
    private static final AppService appService = new AppService();
    private static final FakerService faker = new FakerService();
    private final HttpClient httpClient = new HttpClient();
    private final JsonService jsonService = new JsonService();
    private static String userId;
    private static String token;
    private static String username;
    private static String passwordId;

    @BeforeClass
    public static void initialSetup() throws IOException {
        username = faker.generateName();
        userId = appService.createUser(faker.generateName(), faker.generateEmail(), username);
        token = appService.getToken(username, "123");
        appService.createPassword(userId, token);
        passwordId = appService.getPasswordId(userId, token);
    }

    @Test
    public void showPasswordSuccess() throws IOException {
        jsonService.mountShowPasswordPayload(userId, passwordId);
        File payload = new File("src/test/resources/payloads/password/ShowPassword.json");
        httpClient.post(payload, "/password/show", token)
                .statusCode(200)
                .body("data.decriptedPassword", is(notNullValue()));
    }

    @Test
    public void sendInvalidUserId() throws IOException {
        jsonService.mountShowPasswordPayload("invalid", passwordId);
        File payload = new File("src/test/resources/payloads/password/ShowPassword.json");
        httpClient.post(payload, "/password/show", token)
                .statusCode(400)
                .body("message", is("ID de usuário inválido."));
    }

    @Test
    public void sendInvalidPasswordId() throws IOException {
        jsonService.mountShowPasswordPayload(userId, "invalid");
        File payload = new File("src/test/resources/payloads/password/ShowPassword.json");
        httpClient.post(payload, "/password/show", token)
                .statusCode(400)
                .body("message", is("ID de password inválido."));
    }

    @Test
    public void sendInvalidToken() throws IOException {
        jsonService.mountShowPasswordPayload(userId, passwordId);
        File payload = new File("src/test/resources/payloads/password/ShowPassword.json");
        httpClient.post(payload, "/password/show", "invalid")
                .statusCode(403);
    }
}
