package com.udacity.jwdnd.course1.cloudstorage;
import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Requirements for testing users signup and login flow
 *
 * 1. Write a Selenium test that verifies that the home page is not accessible without logging in.
 *
 * 2. Write a Selenium test that signs up a new user, logs that user in, verifies that they can access the home page,
 *  then logs out and verifies that the home page is no longer accessible.
 *
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupLoginUsersTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private SignupPage signupPage;
    private LoginPage loginPage;
    private HomePage homePage;

    private static final String FIRSTNAME = "Hang";
    private static final String LASTNAME = "Wei";
    private static final String USERNAME = "Xppt";
    private static final String PASSWORD = "123456";

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void homepage_IsNotAccessed_WithoutLoginIn() {
        driver.get("http://localhost:" + this.port + "/home");
        assertFalse(driver.getTitle() == "home");

        driver.get("http://localhost:" + this.port + "/file");
        assertEquals("Login", driver.getTitle());

        // user is permitted to visit signup page without logging in
        driver.get("http://localhost:" + this.port + "/signup");
        assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    public void signupNewUser_LogIn_CanVisitHomepage() throws InterruptedException {
        signup(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        login(USERNAME, PASSWORD);

        assertEquals("Home", driver.getTitle());
        homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.logout();

        assertFalse(driver.getTitle() == "Home");
        assertEquals("Login", driver.getTitle());

    }

    public void signup(String firstname, String lastname, String username, String password) {
        driver.get("http://localhost:" + this.port + "/signup");
        signupPage = new SignupPage(driver);
        signupPage.signup(firstname, lastname, username, password);
    }

    public void login(String username, String password) {

        driver.get("http://localhost:" + this.port + "/login");
        loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }
}
