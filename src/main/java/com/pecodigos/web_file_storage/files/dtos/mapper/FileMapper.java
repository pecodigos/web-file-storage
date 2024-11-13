package com.pecodigos.web_file_storage.files.dtos.mapper;

import com.pecodigos.web_file_storage.files.dtos.FileDTO;
import com.pecodigos.web_file_storage.files.entities.File;
import org.mapstruct.Mapper;

@Mapper
public interface FileMapper {
    File toEntity(FileDTO fileDTO);
    FileDTO toDto(File file);
}
