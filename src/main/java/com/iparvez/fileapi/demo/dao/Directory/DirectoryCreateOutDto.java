package com.iparvez.fileapi.demo.dao.Directory;

import com.iparvez.fileapi.demo.enums.DirectoryEnum;
import com.iparvez.fileapi.demo.models.Directory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
public class DirectoryCreateOutDto {
    @Getter @Setter private Directory directory; 
    @Getter @Setter private DirectoryEnum status; 

    public DirectoryCreateOutDto(DirectoryEnum status){
        this.setStatus(status);
    }

}
