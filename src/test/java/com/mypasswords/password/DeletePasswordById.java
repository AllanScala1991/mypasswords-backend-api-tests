package com.mypasswords.password;

import Utils.AppService;
import Utils.FakerService;
import Utils.HttpClient;
import Utils.JsonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

public class DeletePasswordById {
    private static final AppService appService = new AppService();
    private static final FakerService faker = new FakerService();
    private final HttpClient httpClient = new HttpClient();
    private JsonService jsonService = new JsonService();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static String userId;
    private static String token;
    private static String username;

    @BeforeClass
    public static void initialSetup() throws IOException {
        username = faker.generateName();
        userId = appService.createUser(faker.generateName(), faker.generateEmail(), username);
        token = appService.getToken(username, "123");
    }

    @Test
    public void deletePasswordById() throws IOException{
        appService.createPassword(userId, token);
        String passwordId = appService.getPasswordId(userId, token);
        httpClient.delete("/password/" + passwordId, token)
                .statusCode(200)
                .body("message", is("Registro deletado com sucesso."));
    }

    @Test
    public void sendInvalidPasswordId() {
        httpClient.delete("/password/invalid", token)
                .statusCode(404)
                .body("message", is("Registro de senha não localizado."));
    }

    @Test
    public void sendInvalidToken() throws IOException{
        appService.createPassword(userId, token);
        String passwordId = appService.getPasswordId(userId, token);
        httpClient.delete("/password/" + passwordId, "invalid")
                .statusCode(403);
    }
}
