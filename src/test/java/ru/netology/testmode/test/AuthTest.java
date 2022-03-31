package ru.netology.testmode.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

public class AuthTest { // java -jar artifacts/app-ibank.jar -P:profile=test

    static String errorText = "Неверно указан логин или пароль";

    public static class PageLogin {
        private PageLogin() {
        }

        public static void login(String login, String password) {
            SelenideElement form = $(".form");
            form.$("[data-test-id=login] input").val(login);
            form.$("[data-test-id=password] input").val(password);
            form.$("[data-test-id=action-login]").click();
        }
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1000x800";
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        PageLogin.login(registeredUser.getLogin(), registeredUser.getPassword());
        $("h2.heading").shouldHave(text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        PageLogin.login(notRegisteredUser.getLogin(), notRegisteredUser.getPassword());
        $("[data-test-id=error-notification] .notification__content").should(appear).shouldHave(text(errorText));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        PageLogin.login(blockedUser.getLogin(), blockedUser.getPassword());
        $("[data-test-id=error-notification] .notification__content").should(appear).shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        PageLogin.login(wrongLogin, registeredUser.getPassword());
        $("[data-test-id=error-notification] .notification__content").should(appear).shouldHave(text(errorText));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        PageLogin.login(registeredUser.getLogin(), wrongPassword);
        $("[data-test-id=error-notification] .notification__content").should(appear).shouldHave(text(errorText));
    }
}