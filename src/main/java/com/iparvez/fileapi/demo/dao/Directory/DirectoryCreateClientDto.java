package com.iparvez.fileapi.demo.dao.Directory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*class used when recieving details of directory create from client */

@AllArgsConstructor @NoArgsConstructor
public class DirectoryCreateClientDto {
    @Getter @Setter private String directory_name; 
    @Getter @Setter private Long creator_id, parent_id;
    @Getter @Setter private int accessType;     
}
