package com.iparvez.fileapi.demo.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor @NoArgsConstructor
@Data
@Entity
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; 

    // @Column(unique = true) private String name; 
    @Column(nullable = false)
    private String extension;
    
    
    @OneToOne
    @JoinColumn(name="creator", referencedColumnName = "id")
    private User creator;
    
    @OneToMany(mappedBy = "fileId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chunk> chunks; 

}
