package com.iparvez.fileapi.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iparvez.fileapi.demo.dao.Directory.DirectoryCreateClientDto;
import com.iparvez.fileapi.demo.dao.Directory.DirectoryCreateOutDto;
import com.iparvez.fileapi.demo.dao.Directory.DirectoryGetAllFromParentDto;
import com.iparvez.fileapi.demo.dao.Directory.DirectoryNameUpdateClientDto;
import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.services.DirectoryService;





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
    public ResponseEntity<?> createDirectory(@RequestBody DirectoryCreateClientDto dirCreateClientDto, 
        @AuthenticationPrincipal User user
    ) {
        /* check if directory with same name exists in the current parent */
        try {
            /* get dto from service */
            DirectoryCreateOutDto directoryCreateOutDto = this.dirService.createDirectory(dirCreateClientDto, user); 

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
