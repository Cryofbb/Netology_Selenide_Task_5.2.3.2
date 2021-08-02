package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.*;
import lombok.Data;

import java.util.Locale;

import static io.restassured.RestAssured.*;

public class DataGenerator {
    private DataGenerator(){
    }

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    @Data
    public static class User {
        private final String login;
        private final String password;
        private final String status;
    }
    public static class Registration {
        private Registration() {
        }

        public static User getUser(String status) {
            return new User(loginGen(), passGen(), status);
        }

        public static User getRegisteredUser(String status) {
            var registeredUser = getUser(status);
             sendRequest(registeredUser);
            return registeredUser;
        }
    }
    public static void sendRequest(User user){
        given()
                .spec(requestSpec)
                .body(new User(user.getLogin(), user.getPassword(),user.getStatus()))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }
    public static String loginGen(){
        return faker.pokemon().name();
    }
    public static String passGen(){
        return faker.internet().password();
    }
}
