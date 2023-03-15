package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.security.EncryptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final CredentialService credentialService;
    private final FileService fileService;
    private final NoteService noteService;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public HomeController(CredentialService credentialService, FileService fileService, NoteService noteService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String getHomePage(Note note, Credential credential, Authentication authentication, Model model) {
        String username = authentication.getName();
        int userId = userService.getUser(username).getUserId();
        List<Credential> credentials = credentialService.getAllCredentials(userId);
        List<File> files = fileService.getAllFiles(userId);
        List<Note> notes = noteService.getAllNotes(userId);
        model.addAttribute("credentials", credentials);
        model.addAttribute("files", files);
        model.addAttribute("notes", notes);
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }


    @GetMapping("/result")
    public String getResultPage() {
        return "result";
    }
}
