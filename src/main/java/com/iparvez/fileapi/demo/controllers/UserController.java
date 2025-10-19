package com.iparvez.fileapi.demo.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService; 

    // find user by id
    @GetMapping("/api/user/id/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable long userId) {
        
        Optional<User> user = this.userService.getUserById(userId);
        if(!user.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        return new ResponseEntity<User>(user.get(), HttpStatus.FOUND); 
    }
    

    // find by name
    @GetMapping("/api/user/name/{uName}")
    public ResponseEntity<?> getUserByName(@PathVariable String uName) {
        Optional<User> user = this.userService.getUserByName(uName);
        if(!user.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        return new ResponseEntity<User>(user.get(), HttpStatus.FOUND);
    }
    

    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        boolean userExists = this.userService.getUserByName(user.getUserName()).isPresent(); 
        if(userExists){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); 
        }
        this.userService.createOrUpdateUser(user); 
        
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }
    
    @PutMapping("/api/user/id/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable long userId, @RequestBody User user) {
        boolean userExists = this.userService.getUserById(userId).isPresent(); 
        if(!userExists){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); 
        }
        user.setId(userId);
        this.userService.createOrUpdateUser(user); 
        
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }
    
    @DeleteMapping("/api/user/id/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId){
        boolean userDeleted = this.userService.deleteUser(userId); 
        if(!userDeleted){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        return new ResponseEntity<>(HttpStatus.OK); 
    }
    
    
}
