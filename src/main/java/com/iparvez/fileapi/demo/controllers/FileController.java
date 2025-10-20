package com.iparvez.fileapi.demo.controllers;

import java.util.HexFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.iparvez.fileapi.demo.models.Chunk;
import com.iparvez.fileapi.demo.models.File;
import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.services.ChunkService;
import com.iparvez.fileapi.demo.services.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




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
    @PostMapping(path = "/api/file")
    public ResponseEntity<?> postFile(@RequestBody byte[] blob, 
        @RequestHeader("X-File-Name") String fileName, 
        @RequestHeader("X-Chunk-Index") short chunkIndex, 
        @RequestHeader("X-File-Ext") String extension
    ) {
        try {
            Optional<User> currentUser = this.fileService.getCreator("napoleon"); 
            System.err.println("user read"+ currentUser.toString());
            boolean fileExists= this.fileService.existsByName(fileName); 
            File file = new File();
            if(!fileExists){
                file.setCreator(currentUser.get());
                file.setExtension(extension);
                file.setFileName(fileName);
                this.fileService.createFile(file); 
            }

            file = this.fileService.getFileByName(fileName).get(); 

            Chunk chunk = new Chunk(); 
            chunk.setChunkIndex(chunkIndex);
            chunk.setData(blob);
            chunk.setFile(file);
            this.chunkService.createChunk(chunk); 
            return new ResponseEntity<Chunk>(chunk, HttpStatus.CREATED); 
            

            // return new ResponseEntity<String>(HttpStatus.ACCEPTED); 
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("Error reading BLOB for hex printing: " + e.getMessage());
            return new ResponseEntity<String>("not done", HttpStatus.FORBIDDEN); 
        }

        // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/api/file/{chunkId}")
    public ResponseEntity<?> getChunk(@PathVariable long chunkId){
        try {
            Optional<Chunk> chunk = this.chunkService.findById(chunkId); 
            if(chunk.isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<Chunk>(chunk.get(), HttpStatus.OK); 
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }
    
    
}
