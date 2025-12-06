package com.iparvez.fileapi.demo.services;

import java.util.Optional;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iparvez.fileapi.demo.dao.Directory.DirectoryCreateClientDto;
import com.iparvez.fileapi.demo.dao.Directory.DirectoryCreateOutDto;
import com.iparvez.fileapi.demo.enums.DirectoryEnum;
import com.iparvez.fileapi.demo.models.Directory;
import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.repo.DirectoryRepo;

@Service
public class DirectoryService {
    @Autowired private DirectoryRepo directoryRepo; 

    /* check if directory exists already */
    public boolean checkIf_Directory_Exists_by_parentId_and_creatorId(
        String directoryName, 
        Long parent_id, 
        Long creator_id
    ){
        Long countDir_by_parentName_and_creatorName = this.directoryRepo.count_by_directoryName_and_parentId_and_creatorId(
            directoryName, parent_id, creator_id
        ); 
        
        return countDir_by_parentName_and_creatorName > 0;
    }
    
    /* create or update directory */
    public DirectoryCreateOutDto createDirectory(DirectoryCreateClientDto directoryCreateClientDto){
        try {
            /* create dto for sending out data */
            DirectoryCreateOutDto directoryCreateOutDto = new DirectoryCreateOutDto(); 

            /* get the parent and the creator items */
            Optional<Directory> parent = this.directoryRepo.findById(
                directoryCreateClientDto.getParent_id()
            ); 
            Optional<User> creator = this.directoryRepo.findByCreatorId(
                directoryCreateClientDto.getCreator_id()
            ); 
            /* create the actual directory */
            Directory createdDirectory = new Directory(); 
            createdDirectory.setName(
                directoryCreateClientDto.getDirectory_name()
            );
            createdDirectory.setAccessType(directoryCreateClientDto.getAccessType());
            createdDirectory.setCreator(creator.get());
            createdDirectory.setParent(parent.get());
            createdDirectory.setUsersGrantedRead(new TreeSet<User>());
            createdDirectory.setUsersGrantedWrite(new TreeSet<User>());

            /* update the out dto */
            directoryCreateOutDto.setDirectory(createdDirectory);
            /* update status */
            directoryCreateOutDto.setStatus(DirectoryEnum.CREATED); 
            
            /* return final dto */
            return directoryCreateOutDto ;
        } catch (Exception e) {
            return new DirectoryCreateOutDto(DirectoryEnum.SERVER_ERROR); 
        }
    }
}
