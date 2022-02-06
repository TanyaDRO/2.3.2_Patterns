package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    void Login(String login, String password) {
        $("[data-test-id='login'] input").setValue(login);
        $("[data-test-id='password'] input").setValue(password);
        $("[data-test-id='action-login']").click();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }


    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {

        var registeredUser = getRegisteredUser("active");

        Login(registeredUser.getLogin(), registeredUser.getPassword());
        $(withText("Личный кабинет"))
                .shouldHave(Condition.text("Личный кабинет"),
                        Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {

        var notRegisteredUser = getUser("active");
        Login(notRegisteredUser.getLogin(), notRegisteredUser.getPassword());
        $("[data-test-id='error-notification']")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"),
                        Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {

        var blockedUser = getRegisteredUser("blocked");
        Login(blockedUser.getLogin(), blockedUser.getPassword());
        $("[data-test-id='error-notification']")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"),
                        Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        Login(wrongLogin, registeredUser.getPassword());
        $("[data-test-id='error-notification']")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"),
                        Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        Login(registeredUser.getLogin(), wrongPassword);
        $("[data-test-id='error-notification']")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"),
                        Duration.ofSeconds(5));
    }
}
