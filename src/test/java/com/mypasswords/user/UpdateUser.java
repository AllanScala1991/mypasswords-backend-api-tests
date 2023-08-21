package com.mypasswords.user;

import Utils.AppService;
import Utils.FakerService;
import Utils.HttpClient;
import Utils.JsonService;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UpdateUser {
    private static final AppService appService = new AppService();
    private static final FakerService faker = new FakerService();
    private final HttpClient httpClient = new HttpClient();
    private final JsonService jsonService = new JsonService();
    private static String userId;

    @BeforeClass
    public static void initialSetup() throws IOException {
        userId = appService.createUser(faker.generateName(), faker.generateEmail(), faker.generateName());
    }

    @Test
    public void updateUserSuccessfully() throws IOException {
        String updatedEmail = faker.generateEmail();
        String updatedUsername = faker.generateName();
        jsonService.updateUser(userId, updatedEmail, updatedUsername);
        File payload = new File("src/test/resources/payloads/user/updateUser.json");
        httpClient.put(payload, "/user", "")
                .statusCode(200)
                .body("data.id", is(userId))
                .body("data.email", is(updatedEmail))
                .body("data.username", is(updatedUsername));
    }

    @Test
    public void sendInvalidUserId() throws IOException {
        jsonService.updateUser(UUID.randomUUID().toString(), "updateEmail@email.com", "updateUsername");
        File payload = new File("src/test/resources/payloads/user/updateUser.json");
        httpClient.put(payload, "/user", "")
                .statusCode(400)
                .body("message", is("Usuário não localizado."));
    }

    @Test
    public void sendEmptyId() throws  IOException {
        jsonService.updateUser("", "updateEmail@email.com", "updateUsername");
        File payload = new File("src/test/resources/payloads/user/updateUser.json");
        httpClient.put(payload, "/user", "")
                .statusCode(400)
                .body("message", is("ID inválido ou inexistente."));
    }
}
