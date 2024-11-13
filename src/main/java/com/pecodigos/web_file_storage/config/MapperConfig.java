package com.pecodigos.web_file_storage.config;

import com.pecodigos.web_file_storage.files.dtos.mapper.FileMapper;
import com.pecodigos.web_file_storage.users.dtos.mapper.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }

    @Bean
    public FileMapper fileMapper() {
        return Mappers.getMapper(FileMapper.class);
    }
}
