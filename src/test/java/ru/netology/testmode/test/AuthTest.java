package ru.netology.testmode.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.page.LoginPage;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;

/*
Запуск SUT:
java -jar artifacts/app-ibank.jar -P:profile=test
*/

public class AuthTest {
    private LoginPage loginPage;
    private final String errorText = "Неверно указан логин или пароль";

    @BeforeEach
    private void setup() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1000x800";
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    public void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        var dashboardPage = loginPage.login(registeredUser);

        dashboardPage.heading.shouldHave(text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    public void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");

        loginPage.login(notRegisteredUser);

        loginPage.errorNotification.should(appear).shouldHave(text(errorText));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    public void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        loginPage.login(blockedUser);

        loginPage.errorNotification.should(appear).shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    public void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");

        loginPage.loginWithWrongLogin(registeredUser);

        loginPage.errorNotification.should(appear).shouldHave(text(errorText));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    public void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");

        loginPage.loginWithWrongPassword(registeredUser);

        loginPage.errorNotification.should(appear).shouldHave(text(errorText));
    }
}