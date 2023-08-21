package com.mypasswords.password;

import Utils.AppService;
import Utils.FakerService;
import Utils.HttpClient;
import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class GetAllPasswordsByUserId {
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
    }

    @Test
    public void getAllPasswordsByUserId() throws IOException {
        httpClient.get("/password/user/" + userId, token)
                .log().all()
                .statusCode(200)
                .body("data.size()", is(greaterThan(0)));
    }

    @Test
    public void sendInvalidUserId() throws IOException {
        httpClient.get("/password/user/invalid", token)
                .log().all()
                .statusCode(404)
                .body("message", is("Nenhum registro localizado."));
    }

    @Test
    public void sendInvalidToken() throws IOException {
        httpClient.get("/password/user/" + userId, "invalid")
                .log().all()
                .statusCode(403);
    }
}
