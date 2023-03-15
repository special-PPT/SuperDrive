package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/file")
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
        String username = authentication.getName();
        int userId = userService.getUser(username).getUserId();
        if (fileService.findFileByName(fileUpload.getOriginalFilename()) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sorry, you cannot upload two files with the same name!");
        } else {
            try {
                fileService.createFile(new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), String.valueOf(fileUpload.getSize()), userId, fileUpload.getBytes()));
                model.addAttribute("files", fileService.getAllFiles(userId));
                redirectAttributes.addFlashAttribute("successMessage", "Your file was uploaded successfully.");
            } catch (Exception e) {
                logger.error("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong and the file can not be uploaded. Please try again!");
                return "redirect:/result";
            }
        }
        return "redirect:/result";
    }


    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileId) {

        try {
            File file = fileService.findFileById(fileId);
            MediaType mediaType = MediaType.parseMediaType(file.getContentType());
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(new ByteArrayResource(file.getFileData()));
        } catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message" + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable int fileId, RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(fileId);
            redirectAttributes.addFlashAttribute("successMessage", "File has been deleted successfully.");
            return "redirect:/result";
        } catch (Exception e) {
            logger.error("Cause: " + e.getCause() + ". Message" + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "File can not be deleted, please try again.");
            return "redirect:/result";
        }
    }
}
