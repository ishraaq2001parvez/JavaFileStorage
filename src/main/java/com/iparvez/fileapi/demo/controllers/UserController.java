package com.iparvez.fileapi.demo.controllers;

import java.util.Optional;
import java.util.TreeMap;

import org.antlr.v4.runtime.misc.Pair;
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
public class UserController {
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
        Optional<User> user = this.userService.getUserByUsername(uName);
        if(user.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        return new ResponseEntity<User>(user.get(), HttpStatus.OK);
    }
    
    // register
    @PostMapping("/api/user/register")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        // System.out.println(user.toString());
        boolean userExists = this.userService.getUserByUsername(user.getUsername()).isPresent(); 
        if(userExists){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        }
        this.userService.createOrUpdateUser(user); 
        
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }
    
    // login
    @PostMapping("/api/user/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        ); 
        
        if(!authentication.isAuthenticated()){
            return new ResponseEntity<String>("incorrect credentials", HttpStatus.FORBIDDEN); 
        }
        TreeMap<String, String> loginDetails = new TreeMap<>(); 
        loginDetails.put("userName", user.getUsername()); 
        loginDetails.put("token", jwtService.generateToken(user.getUsername())); 
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
