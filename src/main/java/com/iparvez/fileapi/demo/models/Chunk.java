package com.iparvez.fileapi.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
@Entity
public class Chunk {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id; 

    @ManyToOne
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private FileData fileId; 
    @Lob private byte[] data; 
    
}
