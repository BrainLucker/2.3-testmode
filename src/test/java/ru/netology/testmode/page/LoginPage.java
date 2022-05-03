package ru.netology.testmode.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.testmode.data.DataGenerator;

import static com.codeborne.selenide.Selenide.$;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    public final SelenideElement errorNotification = $("[data-test-id=error-notification] .notification__content");

    public DashboardPage login(DataGenerator.RegistrationDto user) {
        loginField.val(user.getLogin());
        passwordField.val(user.getPassword());
        loginButton.click();

        return new DashboardPage();
    }

    public void loginWithWrongLogin(DataGenerator.RegistrationDto user) {
        var login = getRandomLogin();

        loginField.val(login);
        passwordField.val(user.getPassword());
        loginButton.click();
    }

    public void loginWithWrongPassword(DataGenerator.RegistrationDto user) {
        var password = getRandomPassword();

        loginField.val(user.getLogin());
        passwordField.val(password);
        loginButton.click();
    }
}