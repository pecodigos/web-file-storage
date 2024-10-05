package com.pecodigos.web_file_storage.files.services;

import com.pecodigos.web_file_storage.exceptions.FileNotFoundException;
import com.pecodigos.web_file_storage.exceptions.InvalidFileNameException;
import com.pecodigos.web_file_storage.files.dtos.FileDTO;
import com.pecodigos.web_file_storage.files.entities.File;
import com.pecodigos.web_file_storage.files.repositories.FileRepository;
import com.pecodigos.web_file_storage.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileDTO uploadFile(MultipartFile file, User user) throws IOException {
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.contains("..")) {
            throw new InvalidFileNameException("Invalid file name: " + fileName);
        }

        Path targetLocation = this.fileStorageLocation.resolve(fileName);

        if (Files.exists(targetLocation)) {
            fileName = System.currentTimeMillis() + "_" + fileName;
            targetLocation = this.fileStorageLocation.resolve(fileName);
        }

        Files.copy(file.getInputStream(), targetLocation);

        // Persist data
        var fileEntity = File.builder()
                .name(fileName)
                .path(targetLocation.toString())
                .size(file.getSize())
                .mimeType(file.getContentType())
                .uploadDate(LocalDate.now())
                .user(user)
                .build();

        fileRepository.save(fileEntity);

        return new FileDTO(
                fileEntity.getName(),
                fileEntity.getPath(),
                fileEntity.getSize(),
                LocalDate.now()
        );
    }

    public Resource loadFileAsResource(String fileName, String username) throws IOException {
        var file = fileRepository.findByName(fileName)
                .orElseThrow(() -> new FileNotFoundException("File not found with name: " + fileName));

        if (!file.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You do not have permission to access this file.");
        }

        Path filePath = Paths.get(file.getPath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new NoSuchFileException("File not found: " + filePath);
        }

        return resource;
    }

    public FileDTO getFileById(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));

        return new FileDTO(
                file.getName(),
                file.getPath(),
                file.getSize(),
                file.getUploadDate()
        );
    }

    public List<FileDTO> listAllFiles(String username) {
       List<File> fileList = fileRepository.findByUserUsername(username);

       return fileList.stream().map(file -> new FileDTO(
               file.getName(),
               file.getPath(),
               file.getSize(),
               file.getUploadDate()
       )).toList();
    }

    public void deleteFile(Long id) {
        fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found."));
        fileRepository.deleteById(id);
    }
}
