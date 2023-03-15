package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Requirements for testing add, edit and delete credentials
 *
 * 1. Write a Selenium test that logs in an existing user, creates a credential and
 *  verifies that the credential details are visible in the credential list.
 *
 * 2. Write a Selenium test that logs in an existing user with existing credentials,
 *  clicks the edit credential button on an existing credential, changes the credential data,
 *  saves the changes, and verifies that the changes appear in the credential list.
 *
 * 3. Write a Selenium test that logs in an existing user with existing credentials,
 *  clicks the delete credential button on an existing credential,
 *  and verifies that the credential no longer appears in the credential list.
 *
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddingEditingDeletingCredentialsTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private static final String FIRSTNAME = "Hang";
    private static final String LASTNAME = "Wei";
    private static final String USERNAME = "Xppt";
    private static final String PASSWORD = "123456";

    private static final String OLD_URL = "http://onesimpleexample/com";
    private static final String OLD_PASSWORD = "example";
    private static final String OLD_USERNAME = "pptu";

    private static final String NEW_CREDENTIAL_URL = "http://newexample/com";
    private static final String NEW_CREDENTIAL_PASSWORD = "newexample";
    private static final String NEW_USERNAME = "mmpu";

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
    @Order(1)
    public void addCredential() throws InterruptedException {
        signup(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        login(USERNAME, PASSWORD);

        driver.get("http://localhost:" + this.port + "/home");
        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.goToCredentialTab();

        CredentialTab credentialTab = new CredentialTab(driver);
        credentialTab.addNewCredential(OLD_URL, OLD_PASSWORD, OLD_USERNAME);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.goToCredentialTab();

        Thread.sleep(1000);
        WebElement credentialElement = credentialTab.getCredentialElement(OLD_URL, OLD_USERNAME);
        assertNotNull(credentialElement);

        Thread.sleep(1000);
        String encryptedPassword = credentialTab.getCredentialPasswordField(credentialElement);
        assertNotEquals(OLD_PASSWORD, encryptedPassword);

        Thread.sleep(1000);
        String decryptedPassword = credentialTab.getDialogDecryptedPassword(OLD_URL, OLD_USERNAME);
        assertEquals(OLD_PASSWORD, decryptedPassword);
    }

    @Test
    @Order(2)
    public void testEditCredential() throws InterruptedException {
        login(USERNAME, PASSWORD);
        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.goToCredentialTab();

        Thread.sleep(1000);
        CredentialTab credentialTab = new CredentialTab(driver);
        credentialTab.editCredential(OLD_URL, OLD_USERNAME, NEW_CREDENTIAL_URL, NEW_CREDENTIAL_PASSWORD, NEW_USERNAME);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.goToCredentialTab();

        Thread.sleep(1000);
        WebElement credentialElement = credentialTab.getCredentialElement(NEW_CREDENTIAL_URL, NEW_USERNAME);
        assertNotNull(credentialElement);

        Thread.sleep(1000);
        String encryptedPassword = credentialTab.getCredentialPasswordField(credentialElement);
        assertNotEquals(NEW_CREDENTIAL_PASSWORD, encryptedPassword);

        Thread.sleep(1000);
        String decryptedPassword = credentialTab.getDialogDecryptedPassword(NEW_CREDENTIAL_URL, NEW_USERNAME);
        assertEquals(NEW_CREDENTIAL_PASSWORD, decryptedPassword);
    }

    @Test
    @Order(3)
    public void deleteCredential() throws InterruptedException {
        login(USERNAME, PASSWORD);
        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.goToCredentialTab();

        Thread.sleep(1000);
        CredentialTab credentialTab = new CredentialTab(driver);
        boolean deleteResult = credentialTab.deleteCredential(NEW_CREDENTIAL_URL, NEW_USERNAME);
        assertTrue(deleteResult);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.goToCredentialTab();

        Thread.sleep(1000);
        WebElement credentialElement = credentialTab.getCredentialElement(NEW_CREDENTIAL_URL, NEW_USERNAME);
        assertNull(credentialElement);
    }



    public void signup(String firstname, String lastname, String username, String password) {
        driver.get("http://localhost:" + this.port + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup(firstname, lastname, username, password);
    }

    public void login(String username, String password) {
        driver.get("http://localhost:" + this.port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }
}
