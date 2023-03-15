package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.security.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    private final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    private final CredentialService credentialService;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable int credentialId, RedirectAttributes redirectAttributes) {
        try {
            credentialService.deleteCredential(credentialId);
            redirectAttributes.addFlashAttribute("successMessage", "Note has been deleted successfully.");
            return "redirect:/result";
        } catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message" + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Credential can not be deleted, please try again.");
            return "redirect:/result";
        }
    }

    @PostMapping
    public String createAndUpdateCredential(Credential credential, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        int userId = userService.getUser(username).getUserId();
        credential.setUserId(userId);
        // set the key and password
        String secureKey = generateSecureKey();
        String encryptPassword = encryptionService.encryptValue(credential.getPassword(), secureKey);


        // if credential has been created, update the credential
        if (credential.getCredentialId() != null) {
            try {
                credential.setKey(secureKey);
                credential.setPassword(encryptPassword);
                credentialService.updateCredential(credential);
                redirectAttributes.addFlashAttribute("successMessage", "Your credential was updated successfully.");
                return "redirect:/result";
            } catch (Exception e) {
                logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong and the credential can not be updated. Please try again!");
                return "redirect:/result";
            }
            // else insert the credential to the database
        } else {
            try {
                credentialService.createCredential(credential);
                redirectAttributes.addFlashAttribute("successMessage", "Your credential was added successfully.");
            } catch (Exception e) {
                logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong and the credential can not be added. Please try again!");
                return "redirect:/result";
            }
        }
        return "redirect:/result";
    }

    private String generateSecureKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
