package com.mypasswords.user;

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

public class CreateNewUser {
    private static final FakerService faker = new FakerService();
    private static final HttpClient httpClient = new HttpClient();
    private static JsonService jsonService = new JsonService();

    @Test
    public void createNewUserSuccessfully() throws IOException{
        jsonService.createUser(faker.generateName(), faker.generateEmail(), faker.generateName());
        File payload = new File("src/test/resources/payloads/user/createUser.json");
        httpClient.post(payload, "/user")
                .statusCode(201)
                .body("data.id", is(notNullValue()));
    }

    @Test
    public void trySendPayloadEmptyValue() {
        File emptyPayload = new File("src/test/resources/payloads/user/createUserEmpty.json");
        httpClient.post(emptyPayload, "/user")
                .statusCode(400)
                .body("message", is("Todos os campos devem ser preenchidos."));
    }

    @Test
    public void trySendDuplicatedUsername() throws IOException{
        String username = faker.generateName();
        jsonService.createUser(faker.generateName(), faker.generateEmail(), username);
        File payload = new File("src/test/resources/payloads/user/createUser.json");
        httpClient.post(payload, "/user")
                .statusCode(201);

        jsonService.createUser(faker.generateName(), faker.generateEmail(), username);
        File payloadDuplicated = new File("src/test/resources/payloads/user/createUser.json");
        httpClient.post(payloadDuplicated, "/user")
                .statusCode(400)
                .body("message", is("Já existe um usuário cadastrado com essas informações."));
    }

    @Test
    public void trySendDuplicatedEmail() throws IOException {
        String email = faker.generateName() + "@email.com";
        jsonService.createUser(faker.generateName(), email, faker.generateEmail());
        File payload = new File("src/test/resources/payloads/user/createUser.json");
        httpClient.post(payload, "/user")
                .statusCode(201);

        jsonService.createUser(faker.generateName(), email, faker.generateEmail());
        File payloadDuplicated = new File("src/test/resources/payloads/user/createUser.json");
        httpClient.post(payloadDuplicated, "/user")
                .statusCode(400)
                .body("message", is("Já existe um usuário cadastrado com essas informações."));
    }
}
