package com.mypasswords.password;

import Utils.AppService;
import Utils.FakerService;
import Utils.HttpClient;
import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class GetPasswordByTitle {
    private static final AppService appService = new AppService();
    private static final FakerService faker = new FakerService();
    private final HttpClient httpClient = new HttpClient();
    private static String userId;
    private static String token;
    private static String username;

    @BeforeClass
    public static void initialSetup() throws IOException {
        username = faker.generateName();
        userId = appService.createUser(faker.generateName(), faker.generateEmail(), username);
        token = appService.getToken(username, "123");
        appService.createPassword(userId, token);
        appService.createPassword(userId, token);
    }

    @Test
    public void getPasswordByTitle() {
        String passwordTitle = appService.getPasswordTitle(userId, token);

        httpClient.get("/password/" + passwordTitle, token)
                .statusCode(200)
                .body("data[0].title", is(passwordTitle));
    }

    @Test
    public void sendInvalidProposalTitle() {
        httpClient.get("/password/invalid", token)
                .statusCode(404)
                .body("message", is("Nenhum registro localizado."));
    }

    @Test
    public void sendInvalidToken() {
        String passwordTitle = appService.getPasswordTitle(userId, token);

        httpClient.get("/password/" + passwordTitle, "invalid")
                .statusCode(403);
    }
}
