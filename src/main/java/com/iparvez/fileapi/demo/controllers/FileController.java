package com.iparvez.fileapi.demo.controllers;

import java.util.HexFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.iparvez.fileapi.demo.models.Chunk;
import com.iparvez.fileapi.demo.models.File;
import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.services.ChunkService;
import com.iparvez.fileapi.demo.services.FileService;




@RestController
@CrossOrigin
public class FileController {
    @Autowired private FileService fileService; 
    @Autowired private ChunkService chunkService; 

    // create custom functions for testing



    private void testByteDownload(byte[] blob){
        // byte[] blobBytes = inputStream.readAllBytes();
        System.out.println(blob.length); 
        String hexString = HexFormat.of().formatHex(blob);
        System.out.println(hexString.substring(0, Math.min(hexString.length(), 256)) + "...");
    }



    // mappings
    @PostMapping(path = "/api/file/create")
    public ResponseEntity<?> postFile(@RequestBody byte[] blob, 
        @RequestHeader("X-File-Name") String fileName, 
        @RequestHeader("X-Chunk-Index") short chunkIndex, 
        @RequestHeader("X-File-Ext") String extension,
        @RequestHeader("X-Mime-Type") String mimeType
    ) {
        try {
            Optional<User> currentUser = this.fileService.getCreator("napoleon"); 
            System.out.println("user read"+ currentUser.toString());
            // boolean fileExists= this.fileService.existsByName(fileName); 

            Optional<File> file = this.fileService.getFileByName(fileName); 
            if(file.isEmpty()){
                File newFile = new File();
                newFile.setCreator(currentUser.get());
                newFile.setExtension(extension);
                newFile.setMimeType(mimeType); 
                newFile.setFileName(fileName);
                this.fileService.createOrSaveFile(newFile);
                file = this.fileService.getFileByName(fileName); 
            }
            

            // file = this.fileService.getFileByName(fileName).get(); 

            Chunk chunk = new Chunk(); 
            chunk.setChunkIndex(chunkIndex);
            chunk.setData(blob);
            chunk.setFile(file.get());
            this.chunkService.createChunk(chunk); 
            return new ResponseEntity<Chunk>(chunk, HttpStatus.CREATED); 
            

            // return new ResponseEntity<String>(HttpStatus.ACCEPTED); 
        } catch (Exception e) {
            
            System.err.println("Error reading BLOB for hex printing: " + e.getMessage());
            return new ResponseEntity<String>("not done", HttpStatus.FORBIDDEN); 
        }

        // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/api/chunk/{chunkId}")
    public ResponseEntity<?> getChunk(@PathVariable long chunkId){
        try {
            Optional<Chunk> chunk = this.chunkService.findById(chunkId); 
            if(chunk.isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
            }
            byte[] data = chunk.get().getData(); 
            // HttpHeaders headers = new HttpHeaders(); 
            // headers.add("Content-Length", data.length); 
            return new ResponseEntity<byte[]>(data, HttpStatus.OK) ; 
            // return new ResponseEntity<Chunk>(chunk.get(), HttpStatus.OK); 
        } catch (Exception e) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }

    @GetMapping("/api/file/info/{fileId}")
    public ResponseEntity<?> getFileById(@PathVariable Long fileId) {
        try {
            Optional<File> file = this.fileService.getFileById(fileId); 
            if(file.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND) ; 
            }
            return new ResponseEntity<File>(file.get(), HttpStatus.ACCEPTED); 

        } catch (Exception e) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }

    }
    
    
    
}
