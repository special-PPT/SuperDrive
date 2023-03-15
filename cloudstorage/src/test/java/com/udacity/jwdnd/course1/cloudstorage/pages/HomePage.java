package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id="home-logout")
    private WebElement logoutButton;

    @FindBy(id="nav-notes-tab")
    private WebElement noteTabLink;

    @FindBy(id="nav-credentials-tab")
    private WebElement credentialTabLink;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        logoutButton.submit();
    }

    public void goToNoteTab() {
        noteTabLink.click();
    }

    public void goToCredentialTab() {
        credentialTabLink.click();
    }
}
