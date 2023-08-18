package Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class JsonService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FakerService fakerService = new FakerService();

    public void createUser(String name, String email, String username) throws IOException {
        File file = new File("src/test/resources/payloads/user/createUser.json");
        JsonNode rootNode = objectMapper.readTree(file);
        ((ObjectNode) rootNode).put("name", name);
        ((ObjectNode) rootNode).put("email", email);
        ((ObjectNode) rootNode).put("username", username);
        objectMapper.writeValue(file, rootNode);
    }
}
