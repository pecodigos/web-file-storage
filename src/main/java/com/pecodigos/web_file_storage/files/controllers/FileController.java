package com.pecodigos.web_file_storage.files.controllers;

import com.pecodigos.web_file_storage.files.dtos.FileDTO;
import com.pecodigos.web_file_storage.files.services.FileService;
import com.pecodigos.web_file_storage.users.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private FileService fileService;
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<FileDTO> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        FileDTO uploadFile = fileService.uploadFile(file, userService.getCurrentUser());
        return ResponseEntity.ok(uploadFile);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, Principal principal) throws IOException {
        String username = principal.getName();

        Resource file = fileService.loadFileAsResource(fileName, username);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDTO> getFile(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getFileById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<FileDTO>> getAllFiles(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(fileService.listAllUserFiles(username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable(name = "id") Long id, Principal principal) throws IOException {
        String username = principal.getName();
        fileService.deleteFile(id, username);
        return ResponseEntity.noContent().build();
    }
}