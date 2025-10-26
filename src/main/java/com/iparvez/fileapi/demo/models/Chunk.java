package com.iparvez.fileapi.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
@Entity
public class Chunk {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id; 

    // removed file Id - insufficient storage
    // @ManyToOne
    // @JoinColumn(name = "file_id", referencedColumnName = "id")
    // private FileData fileId; 
    @Column(nullable=false)
    @Getter @Setter
    @Lob private byte[] data; 

    @Column(name = "cid", nullable = false)
    @Getter @Setter
    private short chunkIndex;   
    
    @Getter @Setter
    @ManyToOne
    @JoinColumn(name="fid", referencedColumnName="id", nullable=false)
    private File file; 
}
