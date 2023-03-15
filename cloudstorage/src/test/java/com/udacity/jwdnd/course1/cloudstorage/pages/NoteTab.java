package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class NoteTab {

    @FindBy(className="note-row")
    private List<WebElement> noteElements;

    @FindBy(id="add-new-note-btn")
    private WebElement addNewNoteButton;

    @FindBy(id="note-title")
    private WebElement noteTitleField;

    @FindBy(id="note-description")
    private WebElement noteDescriptionField;

    @FindBy(id="note-save")
    private WebElement noteSaveButton;

    WebDriver webDriver;

    public NoteTab(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
    }

    public void addNewNote(String noteTitle, String noteDescription) {

        new WebDriverWait(webDriver, 1).until(ExpectedConditions.elementToBeClickable(addNewNoteButton)).click();
        new WebDriverWait(webDriver, 1).until(ExpectedConditions.elementToBeClickable(noteSaveButton));

        noteTitleField.sendKeys(noteTitle);
        noteDescriptionField.sendKeys(noteDescription);
        noteSaveButton.click();
    }

    public WebElement getNoteElement(String noteTitle, String noteDescription) {

        for(WebElement noteElement : noteElements){
            WebElement titleElement = noteElement.findElement(By.className("note-title"));
            WebElement descriptionElement = noteElement.findElement(By.className("note-description"));
            String titleElementText = titleElement.getText();
            String descriptionElementText = descriptionElement.getText();

            boolean isTitleSame = titleElement.getText().equals(noteTitle);
            boolean isDescriptionSame = descriptionElement.getText().equals(noteDescription);

            if(titleElement.getText().equals(noteTitle) && descriptionElement.getText().equals(noteDescription)) return noteElement;
        }

        return null;
    }

    public boolean editNote(String noteTitle, String noteDescription, String editedNoteTitle, String editedNoteDescription) {

        WebElement noteElement = getNoteElement(noteTitle, noteDescription);
        if (noteElement == null) {
            return false;
        }

        noteElement.findElement(By.className("note-edit")).click();

        new WebDriverWait(webDriver, 1).until(ExpectedConditions.elementToBeClickable(noteSaveButton));

        noteTitleField.clear();
        noteDescriptionField.clear();

        noteTitleField.sendKeys(editedNoteTitle);
        noteDescriptionField.sendKeys(editedNoteDescription);
        noteSaveButton.click();

        return true;
    }

    public boolean deleteNote(String noteTitle, String noteDescription) {
        WebElement noteElement = getNoteElement(noteTitle, noteDescription);
        if (noteElement == null) {
            return false;
        }

        noteElement.findElement(By.className("note-delete")).click();
        return true;
    }
}
