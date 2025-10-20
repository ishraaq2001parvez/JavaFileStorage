package com.iparvez.fileapi.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.repo.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo; 
    
    public Optional<User> getUserByName(String name){
        Optional<User> user = this.userRepo.findByName(name);
        
        return user; 
    } 
    
    public Optional<User> getUserById(Long id){
        Optional<User> user=  this.userRepo.findById(id); 
        return user; 
    }
    
    public User createOrUpdateUser(User user){
        return this.userRepo.save(user); 
    }
    
    public boolean deleteUser(long id){
        if(!this.userRepo.existsById(id)){
            return false; 
        }
        this.userRepo.deleteById(id);
        return true; 
    }
}
