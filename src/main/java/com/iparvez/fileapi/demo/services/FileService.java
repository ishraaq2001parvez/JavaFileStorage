package com.iparvez.fileapi.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.iparvez.fileapi.demo.models.File;
import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.repo.FileRepo;

@Service
public class FileService {
    @Autowired
    public FileRepo fileRepo; 


    public File createOrSaveFile(File file){
        this.fileRepo.save(file);
        return file;  
    }
    
    public Optional<File> getFileByName(String name){
        Optional<File> file=  this.fileRepo.findByName(name); 
        return file; 
    }
    
    public Optional<File> getFileById(long id){
        Optional<File> file = this.fileRepo.findById(id); 
        return file; 
    }

    public Optional<User> getCreator(String userName){
        Optional<User> creator = this.fileRepo.findCreator(userName); 
        return creator; 
    }
    
    public boolean existsByName(String fileName){
        return this.fileRepo.existsByName(fileName); 
    }
}
