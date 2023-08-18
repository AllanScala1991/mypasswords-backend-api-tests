package com.mypasswords.login;

import Utils.AppService;
import Utils.FakerService;
import Utils.HttpClient;
import Utils.JsonService;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import java.io.File;
import java.io.IOException;

public class Login {
    private static final AppService appService = new AppService();
    private static final FakerService faker = new FakerService();
    private final HttpClient httpClient = new HttpClient();
    private final JsonService jsonService = new JsonService();
    private static String username;

    @BeforeClass
    public static void initialSetup() throws IOException {
        username = faker.generateName();
        appService.createUser(faker.generateName(), faker.generateEmail(), username);
    }

    @Test
    public void userLoginSuccessfully() throws IOException{
        jsonService.login(username, "123");
        File payload = new File("src/test/resources/payloads/login/login.json");
        httpClient.post(payload, "/login")
                .statusCode(200)
                .body("data.token", is(notNullValue()));
    }

    @Test
    public void sendInvalidUsername() throws IOException{
        jsonService.login("invalid", "123");
        File payload = new File("src/test/resources/payloads/login/login.json");
        httpClient.post(payload, "/login")
                .statusCode(400)
                .body("message", is("Usuário ou Senha incorretos."));
    }

    @Test
    public void sendInvalidPassword() throws IOException{
        jsonService.login(username, "invalid");
        File payload = new File("src/test/resources/payloads/login/login.json");
        httpClient.post(payload, "/login")
                .statusCode(400)
                .body("message", is("Usuário ou Senha incorretos."));
    }

    @Test
    public void sendEmptyUsername() throws IOException{
        jsonService.login("", "123");
        File payload = new File("src/test/resources/payloads/login/login.json");
        httpClient.post(payload, "/login")
                .statusCode(400)
                .body("message", is("Os campos de Usuário e Senha são obrigatórios."));
    }

    @Test
    public void sendEmptyPassword() throws IOException{
        jsonService.login(username, "");
        File payload = new File("src/test/resources/payloads/login/login.json");
        httpClient.post(payload, "/login")
                .statusCode(400)
                .body("message", is("Os campos de Usuário e Senha são obrigatórios."));
    }
}
