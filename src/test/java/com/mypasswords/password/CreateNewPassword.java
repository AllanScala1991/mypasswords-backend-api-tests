package com.mypasswords.password;

import Utils.AppService;
import Utils.FakerService;
import Utils.HttpClient;
import Utils.JsonService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;

public class CreateNewPassword {
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
    public void createNewPasswordSuccessfully() throws IOException {
        jsonService.createPassword(userId);
        File payload = new File("src/test/resources/payloads/password/CreatePassword.json");
        httpClient.post(payload, "/password", token)
                .statusCode(201)
                .body("data.userId", is(userId));
    }

    @Test
    public void sendInvalidUserId() throws IOException {
        jsonService.createPassword("invalid");
        File payload = new File("src/test/resources/payloads/password/CreatePassword.json");
        httpClient.post(payload, "/password", token)
                .statusCode(400)
                .body("message", is("Erro ao localizar a chave do usuário."));
    }

    @Test
    public void sendEmptyPayloadValue() throws IOException {
        File file = new File("src/test/resources/payloads/password/EmptyPayload.json");
        JsonNode rootNode = objectMapper.readTree(file);
        ((ObjectNode) rootNode).put("userId", userId);
        objectMapper.writeValue(file, rootNode);

        File payload = new File("src/test/resources/payloads/password/EmptyPayload.json");
        httpClient.post(payload, "/password", token)
                .statusCode(400)
                .body("message", is("Todos os campos devem ser preenchidos."));
    }

    @Test
    public void sendEmptyToken() throws IOException {
        jsonService.createPassword(userId);
        File payload = new File("src/test/resources/payloads/password/CreatePassword.json");
        httpClient.post(payload, "/password", "")
                .statusCode(403);
    }
}
