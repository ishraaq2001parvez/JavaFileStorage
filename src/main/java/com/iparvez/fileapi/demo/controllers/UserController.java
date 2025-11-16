package com.iparvez.fileapi.demo.controllers;

import java.util.Optional;
import java.util.TreeMap;


import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iparvez.fileapi.demo.custom.Pair;
import com.iparvez.fileapi.demo.enums.UserEnum;
import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.services.jwtService;
import com.iparvez.fileapi.demo.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController implements UserEnum{
    @Autowired private UserService userService; 
    
    @Autowired private jwtService jwtService; 
    
    @Autowired AuthenticationManager authenticationManager; 
    
    @GetMapping("/hello")
    public String testPath() {
        return new String("hello");
    }
    
    
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
        Pair<User, TYPE> checkUser = this.userService.getUserByUsername(uName); 
        // if(che)
        if(checkUser.getSecond()==TYPE.NOT_FOUND){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        return new ResponseEntity<User>(checkUser.getFirst(), HttpStatus.OK);
    }
    
    // register
    @PostMapping("/api/user/register")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        // System.out.println(user.toString());
        try{
            Pair<User, UserEnum.TYPE> checkUser = this.userService.getUserByUsername(user.getUsername()); 
            if(checkUser.getSecond()==UserEnum.TYPE.FOUND){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
            }
            this.userService.createOrUpdateUser(user); 
            
            return new ResponseEntity<String>("user has been created", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY); 
        }
        
    }
    
    // login
    @PostMapping("/api/user/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Pair<User, UserEnum.TYPE> checkUser = this.userService.getUserByUsername(user.getUsername()); 
        // System.out.println(checkUser.getFirst().toString());
        if(checkUser.getSecond()==UserEnum.TYPE.SERVER_ERROR){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY); 
        }

        if(checkUser.getSecond()==UserEnum.TYPE.NOT_FOUND){
            // System.out.println("hit breakpoint");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        System.out.println(checkUser.getFirst().getUsername());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getUsername(), 
                user.getPassword()
            )
        ); 
        
        if(!authentication.isAuthenticated()){
            return new ResponseEntity<String>("incorrect credentials", HttpStatus.FORBIDDEN); 
        }
        Pair<String, String> loginDetails= Pair.of(checkUser.getFirst().getUsername(), jwtService.generateToken(user.getUsername())); 
        return new ResponseEntity<>(loginDetails, HttpStatus.OK) ; 
        
        // return entity;
    }
    
    
    // update
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
