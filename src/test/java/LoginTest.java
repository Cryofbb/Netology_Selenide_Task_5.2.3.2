import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.DataGenerator;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;


public class LoginTest {

    @BeforeEach
    public void setUpAll() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var user = getRegisteredUser("active");
        $("[data-test-id= login] .input__control").setValue(user.getLogin());
        $("[data-test-id= password] .input__control").setValue(user.getPassword());
        $("[data-test-id= action-login]").click();
        $("[id = root]").shouldHave(exactText("Личный кабинет"));
    }

    @Test
    @DisplayName("Login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var user = getUser("active");
        $("[data-test-id= login] .input__control").setValue(user.getLogin());
        $("[data-test-id= password] .input__control").setValue(user.getPassword());
        $("[data-test-id= action-login]").click();
        $(".notification__content").shouldBe(visible).shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var user = getRegisteredUser("blocked");
        $("[data-test-id= login] .input__control").setValue(user.getLogin());
        $("[data-test-id= password] .input__control").setValue(user.getPassword());
        $("[data-test-id= action-login]").click();
        $(".notification__content").shouldBe(visible).shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var user = getRegisteredUser("active");
        $("[data-test-id= login] .input__control").setValue(DataGenerator.loginGen());
        $("[data-test-id= password] .input__control").setValue(user.getPassword());
        $("[data-test-id= action-login]").click();
        $(".notification__content").shouldBe(visible).shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var user = getRegisteredUser("active");
        $("[data-test-id= login] .input__control").setValue(user.getLogin());
        $("[data-test-id= password] .input__control").setValue(DataGenerator.passGen());
        $("[data-test-id= action-login]").click();
        $(".notification__content").shouldBe(visible).shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }
}
