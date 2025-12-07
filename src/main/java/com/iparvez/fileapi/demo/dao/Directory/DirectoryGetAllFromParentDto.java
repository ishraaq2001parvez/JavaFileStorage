package com.iparvez.fileapi.demo.dao.Directory;

import java.util.List;

import com.iparvez.fileapi.demo.enums.DirectoryEnum;
import com.iparvez.fileapi.demo.models.Directory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
public class DirectoryGetAllFromParentDto {
    @Getter @Setter private List<Directory> directories; 
    @Getter @Setter private DirectoryEnum status;
}
