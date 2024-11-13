package com.pecodigos.web_file_storage.files.entities;

import com.pecodigos.web_file_storage.users.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class File implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String path;
    private long size;
    private String mimeType;

    private LocalDate uploadDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
