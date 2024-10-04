package com.pecodigos.web_file_storage.files.controllers;

import com.pecodigos.web_file_storage.files.dtos.FileDTO;
import com.pecodigos.web_file_storage.files.services.FileService;
import com.pecodigos.web_file_storage.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<FileDTO> uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        FileDTO uploadFile = fileService.uploadFile(file, userService.getCurrentUser());
        return ResponseEntity.ok(uploadFile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDTO> getFile(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getFileById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<FileDTO>> getAllFiles() {
        return ResponseEntity.ok(fileService.listAllFiles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}