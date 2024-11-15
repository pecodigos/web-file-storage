package com.pecodigos.web_file_storage.files.services;

import com.pecodigos.web_file_storage.exceptions.FileNotFoundException;
import com.pecodigos.web_file_storage.exceptions.InvalidFileNameException;
import com.pecodigos.web_file_storage.files.dtos.FileDTO;
import com.pecodigos.web_file_storage.files.dtos.mapper.FileMapper;
import com.pecodigos.web_file_storage.files.entities.File;
import com.pecodigos.web_file_storage.files.repositories.FileRepository;
import com.pecodigos.web_file_storage.users.dtos.UserDTO;
import com.pecodigos.web_file_storage.users.dtos.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class FileService {

    private FileRepository fileRepository;
    private FileMapper fileMapper;
    private UserMapper userMapper;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileDTO uploadFile(MultipartFile file, UserDTO userDTO) throws IOException {
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.contains("..")) {
            throw new InvalidFileNameException("Invalid file name: " + fileName);
        }

        Path directoryPath = Paths.get("uploads", userDTO.id().toString());

        if (!Files.exists(directoryPath)) {
            var createdFolder = new java.io.File("uploads/" + userDTO.id()).mkdir();
            if (!createdFolder) {
                throw new IOException("Failed to create directory: " + directoryPath);
            }
        }

        var targetLocation = this.fileStorageLocation.resolve(userDTO.id() + "/" + fileName);

        if (Files.exists(targetLocation)) {
            fileName = System.currentTimeMillis() + "_" + fileName;
            targetLocation = this.fileStorageLocation.resolve(fileName);
        }

        Files.copy(file.getInputStream(), targetLocation);

        var fileEntity = File.builder()
                .name(fileName)
                .path(targetLocation.toString())
                .size(file.getSize())
                .mimeType(file.getContentType())
                .uploadDate(LocalDate.now())
                .user(userMapper.toEntity(userDTO))
                .build();

        fileRepository.save(fileEntity);
        return fileMapper.toDto(fileEntity);
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

        return fileMapper.toDto(file);
    }

    public List<FileDTO> listAllUserFiles(String username) {
        var files = fileRepository.findByUserUsername(username);

        return files.stream()
                .map(fileMapper::toDto)
                .toList();
    }

    public void deleteFile(Long id, String username) throws IOException {
        var file = fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));

        if (!file.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You do not have permission to delete this file.");
        }

        Path filePath = Paths.get(file.getPath()).normalize();
        Files.deleteIfExists(filePath);

        fileRepository.delete(file);
    }
}
