package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CredentialTab {

    @FindBy(className="credential-row")
    private List<WebElement> credentialElements;

    @FindBy(id="add-new-credential-btn")
    private WebElement addNewCredentialButton;

    @FindBy(id="credential-url")
    private WebElement credentialUrlField;

    @FindBy(id="credential-password")
    private WebElement credentialPasswordField;

    @FindBy(id="credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id="credential-save")
    private WebElement credentialSaveButton;

    WebDriver webDriver;

    public CredentialTab(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
    }

    public void addNewCredential(String url, String password, String username) {

        new WebDriverWait(webDriver, 1).until(ExpectedConditions.elementToBeClickable(addNewCredentialButton)).click();
        new WebDriverWait(webDriver, 1).until(ExpectedConditions.elementToBeClickable(credentialSaveButton));

        credentialUrlField.sendKeys(url);
        credentialPasswordField.sendKeys(password);
        credentialUsernameField.sendKeys(username);
        credentialSaveButton.click();
    }

    public WebElement getCredentialElement(String url, String username) {

        for(WebElement credentialElement : credentialElements){
            WebElement urlElement = credentialElement.findElement(By.className("credential-url"));
            WebElement usernameElement = credentialElement.findElement(By.className("credential-username"));
            String urlElementText = urlElement.getText();
            String passwordElementText = usernameElement.getText();

            boolean isUrlSame = urlElement.getText().equals(url);
            boolean isPasswordSame = usernameElement.getText().equals(username);

            if(urlElement.getText().equals(url) && usernameElement.getText().equals(username)) return credentialElement;
        }

        return null;
    }

    public String getCredentialPasswordField(WebElement credentialElement) {
        WebElement passwordField = credentialElement.findElement(By.className("credential-password"));

        if (passwordField != null) {
            return passwordField.getText();
        }
        return null;
    }

    public String getDialogDecryptedPassword(String url, String username) {
        WebElement credentialElement = getCredentialElement(url, username);

        if (credentialElement == null) {
            return null;
        }
        credentialElement.findElement(By.className("credential-edit")).click();
        new WebDriverWait(webDriver, 1).until(ExpectedConditions.elementToBeClickable(credentialSaveButton));

        String password = credentialPasswordField.getAttribute("value");
        return password;
    }

    public boolean editCredential(String oldUrl, String oldUsername, String newUrl, String newPassword, String newUsername) {

        WebElement credentialElement = getCredentialElement(oldUrl, oldUsername);
        if (credentialElement == null) {
            return false;
        }

        credentialElement.findElement(By.className("credential-edit")).click();

        new WebDriverWait(webDriver, 1).until(ExpectedConditions.elementToBeClickable(credentialSaveButton));

        credentialUrlField.clear();
        credentialPasswordField.clear();
        credentialUsernameField.clear();

        credentialUrlField.sendKeys(newUrl);
        credentialPasswordField.sendKeys(newPassword);
        credentialUsernameField.sendKeys(newUsername);
        credentialSaveButton.click();

        return true;
    }

    public boolean deleteCredential(String url, String password) {
        WebElement credentialElement = getCredentialElement(url, password);
        if (credentialElement == null) {
            return false;
        }

        credentialElement.findElement(By.className("credential-delete")).click();
        return true;
    }
}
