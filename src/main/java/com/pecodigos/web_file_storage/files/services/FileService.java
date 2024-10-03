package com.pecodigos.web_file_storage.files.services;

import com.pecodigos.web_file_storage.exceptions.FileNotFoundException;
import com.pecodigos.web_file_storage.exceptions.InvalidFileNameException;
import com.pecodigos.web_file_storage.files.dtos.FileDTO;
import com.pecodigos.web_file_storage.files.entities.File;
import com.pecodigos.web_file_storage.files.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileDTO uploadFile(MultipartFile file) throws IOException {
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
                .build();

        fileRepository.save(fileEntity);

        return new FileDTO(
                fileName,
                file.getSize(),
                LocalDate.now().toString()
        );
    }

    public FileDTO getFileById(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));

        return new FileDTO(
                file.getName(),
                file.getSize(),
                file.getUploadDate().toString()
        );
    }

    public List<FileDTO> listAllFiles() {
       List<File> fileList = fileRepository.findAll();

       return fileList.stream().map(file -> new FileDTO(
               file.getName(),
               file.getSize(),
               file.getUploadDate().toString()
       )).toList();
    }

    public void deleteFile(Long id) {
        Optional<File> file = fileRepository.findById(id);

        if (file.isEmpty()) {
            throw new NoSuchElementException("File not found.");
        }

        fileRepository.delete(file.get());
    }
}
