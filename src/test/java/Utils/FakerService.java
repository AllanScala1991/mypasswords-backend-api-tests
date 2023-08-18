package Utils;

import com.github.javafaker.Faker;

public class FakerService {
    private Faker faker = new Faker();

    public String generateName() {
        return faker.name().firstName();
    }

    public String generateEmail() {
        return faker.name().firstName() + "@email.com";
    }
}
