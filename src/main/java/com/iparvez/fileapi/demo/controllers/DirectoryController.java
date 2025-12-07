package com.iparvez.fileapi.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iparvez.fileapi.demo.dao.Directory.DirectoryCreateClientDto;
import com.iparvez.fileapi.demo.dao.Directory.DirectoryCreateOutDto;
import com.iparvez.fileapi.demo.dao.Directory.DirectoryGetAllFromParentDto;
import com.iparvez.fileapi.demo.dao.Directory.DirectoryNameUpdateClientDto;
import com.iparvez.fileapi.demo.enums.DirectoryEnum;
import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.services.DirectoryService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
public class DirectoryController {
    @Autowired private DirectoryService dirService; 

    @PostMapping("/api/dir/test")
    public ResponseEntity<?> testDirectoryCreateParent(
        @AuthenticationPrincipal User user
    ) {
        try {
            DirectoryCreateOutDto directoryCreateOutDto = this.dirService.testDirectoryCreate(user); 
            return new ResponseEntity<>(directoryCreateOutDto, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }

    /* post mapping for directory create */
    @PostMapping("/api/dir/create")
    public ResponseEntity<?> createDirectory(@RequestBody DirectoryCreateClientDto dirCreateClientDto) {
        /* check if directory with same name exists in the current parent */
        try {
    
            boolean exists_by_dirName_parentName_and_creatorName = this.dirService.checkIf_Directory_Exists_by_parentId_and_creatorId(
                dirCreateClientDto.getDirectory_name(), 
                dirCreateClientDto.getParent_id(),
                dirCreateClientDto.getCreator_id()
            ); 
            /* create and out dto to send status and data */
            DirectoryCreateOutDto directoryCreateOutDto = new DirectoryCreateOutDto();
            /* if exists, return a code of 404 */
            if(exists_by_dirName_parentName_and_creatorName){
                directoryCreateOutDto.setStatus(DirectoryEnum.NOT_ACCEPTABLE);
                return new ResponseEntity<>( directoryCreateOutDto, HttpStatus.BAD_REQUEST); 
            }
            directoryCreateOutDto = this.dirService.createDirectory(dirCreateClientDto); 

            return new ResponseEntity<>(directoryCreateOutDto, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  
        }
        
    }

    /* put mapping for updating name of directory */
    @PutMapping("/api/dir/name/{dirId}")
    public ResponseEntity<?> updateDirectoryName(
        @PathVariable Long dirId, 
        @RequestBody DirectoryNameUpdateClientDto directoryNameUpdateClientDto, 
        @AuthenticationPrincipal User user
    ) {
        try {
            /* test directory name update */
            DirectoryCreateOutDto directoryCreateOutDto = this.dirService.updateDirectoryName(
                dirId, 
                directoryNameUpdateClientDto.getUpdatedName(), 
                user
            ); 
            return new ResponseEntity<>(directoryCreateOutDto, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }

    /* put mapping for updating read and write access */
    @PutMapping("/api/dir/access/{userId}")
    public String updateDirectoryAccess(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }

    /* get all directory contents */
    @GetMapping("/api/dir/getContents")
    public ResponseEntity<?> getContentsofDirectory(
        @RequestParam Long parentId, 
        @AuthenticationPrincipal User user
    ) {
        try {
            DirectoryGetAllFromParentDto directoryGetAllFromParentDto; 
            if(parentId == 0){
                directoryGetAllFromParentDto = this.dirService.getAllDirectoriesFromParent(
                    user.getId()
                ); 
            }
            else{
                directoryGetAllFromParentDto = this.dirService.getAllDirectoriesFromParent(
                    user.getId(), parentId
                ); 
            }
            return new ResponseEntity<>(directoryGetAllFromParentDto, HttpStatus.OK); 
        } catch (Exception e) {
            System.err.println("getContentsofDirectory exception\n : "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }
    
    
    
}
