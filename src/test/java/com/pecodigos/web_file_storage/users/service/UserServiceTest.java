package com.pecodigos.web_file_storage.users.service;

import com.pecodigos.web_file_storage.users.entities.User;
import com.pecodigos.web_file_storage.users.enums.Role;
import com.pecodigos.web_file_storage.users.repositories.UserRepository;
import com.pecodigos.web_file_storage.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .name("Pedro Henrique")
                .username("pecodigos")
                .email("pecodigos@gmail.com")
                .password("pecodigos123")
                .role(Role.COMMON)
                .build();
    }
}
