package com.iparvez.fileapi.demo.services;

import java.util.Optional;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iparvez.fileapi.demo.dao.Directory.DirectoryCreateClientDto;
import com.iparvez.fileapi.demo.dao.Directory.DirectoryCreateOutDto;
import com.iparvez.fileapi.demo.dao.Directory.DirectoryGetAllFromParentDto;
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

            /* save created directory */
            this.directoryRepo.save(createdDirectory) ;
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

    /* test directory create */
    public DirectoryCreateOutDto testDirectoryCreate(User creator){
        try {
            /* create dto for sending back data */
            DirectoryCreateOutDto directoryCreateOutDto = new DirectoryCreateOutDto(); 
            /* create actual directory */
            Directory createdDirectory = new Directory(); 
            createdDirectory.setAccessType(0);
            createdDirectory.setParent(null);
            createdDirectory.setName(creator.getUsername());
            createdDirectory.setCreator(creator);
            createdDirectory.setUsersGrantedRead(new TreeSet<User>());
            createdDirectory.setUsersGrantedWrite(new TreeSet<User>());
            /* save created directory */
            this.directoryRepo.save(createdDirectory); 
            /* save dto */
            directoryCreateOutDto.setDirectory(createdDirectory);
            directoryCreateOutDto.setStatus(DirectoryEnum.CREATED);
            /* return dto */
            return directoryCreateOutDto ;

        } catch (Exception e) {
            System.err.println(e.toString());
            return new DirectoryCreateOutDto(DirectoryEnum.SERVER_ERROR) ;
        }
    }

    /* update directory name */
    public DirectoryCreateOutDto updateDirectoryName(Long dirId, String updatedName, User creator){
        try {
            /* create dto for sending to client */
            DirectoryCreateOutDto directoryCreateOutDto = new DirectoryCreateOutDto(); 
            /* search for directory */
            Optional<Directory> dirtoUpdate = this.directoryRepo.findById(dirId); 
            /* if there is no directory with the current id */
            if(dirtoUpdate.isEmpty()){
                /* set dto status to not found, return it */
                directoryCreateOutDto.setStatus(DirectoryEnum.NOT_FOUND);
                return directoryCreateOutDto; 
            }
            /* check if user is not allowed to update this */
            /* he will only be allowed to update if he has update access */
            /* or if he is the owner */
            if(
                !dirtoUpdate.get().getUsersGrantedWrite().contains(creator) && 
                dirtoUpdate.get().getCreator().getId()!=creator.getId()
            ){
                /* return unauthorised */
                directoryCreateOutDto.setStatus((DirectoryEnum.NOT_AUTHORISED));
                return directoryCreateOutDto; 
            }
            /* update name */
            dirtoUpdate.get().setName(updatedName);
            /* save directory */
            this.directoryRepo.save(dirtoUpdate.get()); 
            /* set dto variables  and return*/
            directoryCreateOutDto.setDirectory(dirtoUpdate.get());
            directoryCreateOutDto.setStatus(DirectoryEnum.UPDATED);
            return directoryCreateOutDto; 

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new DirectoryCreateOutDto(DirectoryEnum.SERVER_ERROR); 
        }
    }

    /* get directory contents */
    /* with parent */
    public DirectoryGetAllFromParentDto getAllDirectoriesFromParent(
        Long creatorId
    ){
        /* create dto passing to client */
        DirectoryGetAllFromParentDto directoryGetAllFromParentDto = new DirectoryGetAllFromParentDto(); 
        try {
            /* we determined that joins will be much slower, so we fetch parent first */
            /* then we fetch subdirectories */
            Optional<Directory> parentDir = this.directoryRepo.findParentByCreatorId(creatorId); 
            /* if there's no such parent, return not found */
            if(parentDir.isEmpty()){
                directoryGetAllFromParentDto.setStatus(DirectoryEnum.NOT_FOUND);
                return directoryGetAllFromParentDto; 
            }
            /* if parent directory exists, then get all sub directories  */
            directoryGetAllFromParentDto.setDirectories(
                this.directoryRepo.findAll_by_parentId_and_creatorId(
                    parentDir.get().getDir_id(), 
                    creatorId
                )
            );
            /* set status and return */
            directoryGetAllFromParentDto.setStatus(DirectoryEnum.FOUND);
            return directoryGetAllFromParentDto; 
        } catch (Exception e) {
            System.err.println("getAllDirectoriesFromParent exception\n : "+e.getMessage());
            directoryGetAllFromParentDto.setStatus(DirectoryEnum.SERVER_ERROR);
            return directoryGetAllFromParentDto; 
        }
    }

    /*without parent */
    public DirectoryGetAllFromParentDto getAllDirectoriesFromParent(
        Long creatorId, Long parentId
    ){
        /* create dto passing to client */
        DirectoryGetAllFromParentDto directoryGetAllFromParentDto = new DirectoryGetAllFromParentDto(); 
        try {
            Optional<Directory> parentDir = this.directoryRepo.findById(parentId); 
            /* if there's no such parent, return not found */
            if(parentDir.isEmpty()){
                directoryGetAllFromParentDto.setStatus(DirectoryEnum.NOT_FOUND);
                return directoryGetAllFromParentDto; 
            }
            /* if parent directory exists, then get all sub directories  */
            directoryGetAllFromParentDto.setDirectories(
                this.directoryRepo.findAll_by_parentId_and_creatorId(
                    parentId, 
                    creatorId
                )
            );
            /* set status and return */
            directoryGetAllFromParentDto.setStatus(DirectoryEnum.FOUND);
            return directoryGetAllFromParentDto; 
        } catch (Exception e) {
            System.err.println("getAllDirectoriesFromParent exception\n : "+e.getMessage());
            directoryGetAllFromParentDto.setStatus(DirectoryEnum.SERVER_ERROR);
            return directoryGetAllFromParentDto; 
        }
    }
}
