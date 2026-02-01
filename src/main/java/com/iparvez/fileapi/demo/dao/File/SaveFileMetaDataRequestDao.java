package com.iparvez.fileapi.demo.dao.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
public class SaveFileMetaDataRequestDao {
    @Getter @Setter
    private String fileName, extension, mimeType, fileSignature; 
    @Getter @Setter private byte[] fileHeader, fileFooter; 

    @Getter @Setter private Integer chunkCount, accessType ;
    @Getter @Setter private Long dirId; 

}
