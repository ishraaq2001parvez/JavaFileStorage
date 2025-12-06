package com.iparvez.fileapi.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iparvez.fileapi.demo.dao.Directory.DirectoryCreateClientDto;
import com.iparvez.fileapi.demo.dao.Directory.DirectoryCreateOutDto;
import com.iparvez.fileapi.demo.enums.DirectoryEnum;
import com.iparvez.fileapi.demo.services.DirectoryService;



@RestController
public class DirectoryController {
    @Autowired private DirectoryService dirService; 

    @GetMapping("/api/dir/test")
    public String getMethodName(@RequestParam String param) {
        return new String("testing directory");
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
    
    
}
