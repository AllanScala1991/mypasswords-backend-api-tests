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

public class UpdatePasswordById {
    private static final AppService appService = new AppService();
    private static final FakerService faker = new FakerService();
    private final HttpClient httpClient = new HttpClient();
    private JsonService jsonService = new JsonService();
    private static String userId;
    private static String token;
    private static String username;

    @BeforeClass
    public static void initialSetup() throws IOException {
        username = faker.generateName();
        userId = appService.createUser(faker.generateName(), faker.generateEmail(), username);
        token = appService.getToken(username, "123");
        appService.createPassword(userId, token);
    }

    @Test
    public void updatePasswordById() throws IOException {
        String passwordId = appService.getPasswordId(userId, token);

        jsonService.updatePassword(passwordId, userId);

        File payload = new File("src/test/resources/payloads/password/UpdatePassword.json");

        httpClient.put(payload, "/password", token)
                .statusCode(200)
                .body("message", is("Registro atualizado com sucesso."));
    }

    @Test
    public void sendInvalidPasswordId() throws IOException {
        jsonService.updatePassword("invalid", userId);

        File payload = new File("src/test/resources/payloads/password/UpdatePassword.json");

        httpClient.put(payload, "/password", token)
                .statusCode(404)
                .body("message", is("Registro de senha não localizado."));
    }

    @Test
    public void sendInvalidUserId() throws IOException {
        String passwordId = appService.getPasswordId(userId, token);

        jsonService.updatePassword(passwordId, "invalid");

        File payload = new File("src/test/resources/payloads/password/UpdatePassword.json");

        httpClient.put(payload, "/password", token)
                .log().all()
                .statusCode(400)
                .body("message", is("Erro ao localizar a chave do usuário."));
    }

    @Test
    public void sendPayloadEmptyValue() throws IOException {
        String passwordId = appService.getPasswordId(userId, token);

        jsonService.updatePassword(passwordId, userId);

        File payload = new File("src/test/resources/payloads/password/UpdateEmptyPayload.json");

        httpClient.put(payload, "/password", token)
                .statusCode(400)
                .body("message", is("Todos os campos devem ser preenchidos."));
    }
}
