package com.pecodigos.web_file_storage.files.repositories;

import com.pecodigos.web_file_storage.files.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByName(String name);
}
