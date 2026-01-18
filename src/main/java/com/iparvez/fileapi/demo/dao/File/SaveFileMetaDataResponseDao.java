package com.iparvez.fileapi.demo.dao.File;

import com.iparvez.fileapi.demo.enums.FileEnum;
import com.iparvez.fileapi.demo.models.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
public class SaveFileMetaDataResponseDao {
    @Getter @Setter private File file; 
    @Getter @Setter private FileEnum status; 
}
