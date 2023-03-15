package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.NoteTab;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
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
 * Requirements for testing add, edit and delete notes
 *
 * 1. Write a Selenium test that logs in an existing user, creates a note and verifies
 *  that the note details are visible in the note list.
 *
 * 2. Write a Selenium test that logs in an existing user with existing notes,
 *  clicks the edit note button on an existing note, changes the note data,
 *  saves the changes, and verifies that the changes appear in the note list.
 *
 * 3. Write a Selenium test that logs in an existing user with existing notes,
 *  clicks the delete note button on an existing note, and verifies that
 *  the note no longer appears in the note list.
 *
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddingEditingDeletingNotesTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private static final String FIRSTNAME = "Hang";
    private static final String LASTNAME = "Wei";
    private static final String USERNAME = "Xppt";
    private static final String PASSWORD = "123456";

    // the title has length limitation, MAX 20 characters.
    private static final String NOTE_TITLE = "Tasks To do";
    private static final String NOTE_DESCRIPTION = "Time Limitation: Before Summer";
    private static final String EDITED_NOTE_TITLE = "Tasks Has Done";
    private static final String EDITED_NOTE_DESCRIPTION = "Time: Now";

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
    public void testAddNote() throws InterruptedException {
        signup(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
        login(USERNAME, PASSWORD);

        driver.get("http://localhost:" + this.port + "/home");
        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.goToNoteTab();

        NoteTab noteTab = new NoteTab(driver);
        noteTab.addNewNote(NOTE_TITLE, NOTE_DESCRIPTION);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.goToNoteTab();

        Thread.sleep(1000);
        WebElement noteElement = noteTab.getNoteElement(NOTE_TITLE, NOTE_DESCRIPTION);

        assertNotNull(noteElement);

    }

    @Test
    @Order(2)
    public void testEditNote() throws InterruptedException {
        login(USERNAME, PASSWORD);
        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.goToNoteTab();

        Thread.sleep(1000);
        NoteTab noteTab = new NoteTab(driver);
        noteTab.editNote(NOTE_TITLE, NOTE_DESCRIPTION, EDITED_NOTE_TITLE, EDITED_NOTE_DESCRIPTION);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.goToNoteTab();

        Thread.sleep(1000);
        WebElement noteElement = noteTab.getNoteElement(EDITED_NOTE_TITLE, EDITED_NOTE_DESCRIPTION);
        assertNotNull(noteElement);
    }

    @Test
    @Order(3)
    public void deleteNote() throws InterruptedException {
        login(USERNAME, PASSWORD);
        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.goToNoteTab();

        Thread.sleep(1000);
        NoteTab noteTab = new NoteTab(driver);
        boolean deleteResult = noteTab.deleteNote(EDITED_NOTE_TITLE, EDITED_NOTE_DESCRIPTION);
        assertTrue(deleteResult);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.goToNoteTab();

        Thread.sleep(1000);
        WebElement noteElement = noteTab.getNoteElement(EDITED_NOTE_TITLE, EDITED_NOTE_DESCRIPTION);
        assertNull(noteElement);
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
