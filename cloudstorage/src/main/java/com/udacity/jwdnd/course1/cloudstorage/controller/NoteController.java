package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/note")
public class NoteController {

    private final Logger logger = LoggerFactory.getLogger(NoteController.class);

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable int noteId, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("successMessage", "Note has been deleted successfully.");
            return "redirect:/result";
        } catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message" + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Note can not be deleted, please try again.");
            return "redirect:/result";
        }
    }

    @PostMapping
    public String createAndUpdateNote(Note note, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        int userId = userService.getUser(username).getUserId();
        note.setUserId(userId);

        // if note has been created, update the note
        if (note.getNoteId() != null) {
            try {
                noteService.updateNote(note);
                // model.addAttribute("notes", noteService.getAllNotes(userId));
                redirectAttributes.addFlashAttribute("successMessage", "Your note was updated successfully.");
                return "redirect:/result";
            } catch (Exception e) {
                logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong and the note can not be updated. Please try again!");
                return "redirect:/result";
            }
            // else insert the note to the database
        } else {
            try {
                noteService.createNote(note);
                //model.addAttribute("notes", noteService.getAllNotes(userId));
                redirectAttributes.addFlashAttribute("successMessage", "Your note was added successfully.");
            } catch (Exception e) {
                logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong and the note can not be added. Please try again!");
                return "redirect:/result";
            }
        }
        return "redirect:/result";
    }
}
