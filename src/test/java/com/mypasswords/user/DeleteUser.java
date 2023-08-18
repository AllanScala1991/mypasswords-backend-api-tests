package com.mypasswords.user;

import Utils.AppService;
import Utils.FakerService;
import Utils.HttpClient;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.UUID;

public class DeleteUser {
    private static final AppService appService = new AppService();
    private static final FakerService faker = new FakerService();
    private final HttpClient httpClient = new HttpClient();
    private static String userId;

    @BeforeClass
    public static void initialSetup() throws IOException {
        userId = appService.createUser(faker.generateName(), faker.generateEmail(), faker.generateName());
    }

    @Test
    public void deleteUserSuccessfully() {
        System.out.println(userId);
        httpClient.delete("/user/" + userId)
                .statusCode(200)
                .body("message", is("Usuário deletado com sucesso."));
    }

    @Test
    public void sendInvalidUserId() {
        httpClient.delete("/user/" + UUID.randomUUID().toString())
                .statusCode(400)
                .body("message", is("Usuário não localizado."));
    }
}
