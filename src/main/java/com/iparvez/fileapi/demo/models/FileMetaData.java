package com.iparvez.fileapi.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
public class FileMetaData {
    // id 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    @Column(name = "id")
    private Long meta_id; 

    // chunk count
    @Column(name = "chunks")
    @Getter @Setter private Integer chunkCount ;

    // metadata
    @Lob
    @Column(name = "metadata_json", length = 100000)
    @Getter @Setter private String metaDataJson;
    
    // file header
    @Lob
    @Column(name = "file_header",  length = 100000)
    @Getter @Setter private byte[] fileHeader; 

    // file footer
    @Lob
    @Column(name = "file_footer", length = 100000)
    @Getter @Setter private byte[] fileFooter; 

    // file signature
    @Column(name = "file_signature")
    private String fileSignature; 
}
