package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
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
            String login = loginGen();
            String password = passGen();
            var user = new User(login, password, status);
            return user;
        }

        public static User getRegisteredUser(String status) {
            var registeredUser = getUser(status);
             sendRequest(registeredUser);
            return registeredUser;
        }
    }
    public static void sendRequest(User user){
        given()
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new User(user.getLogin(), user.getPassword(),user.getStatus())) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users"); // на какой путь, относительно BaseUri отправляем запрос
    }
    public static String loginGen(){
        String login = faker.pokemon().name();
        return login;
    }
    public static String passGen(){
        String password = faker.internet().password();
        return password;
    }
}
