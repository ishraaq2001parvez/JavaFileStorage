package com.iparvez.fileapi.demo.controllers;


import java.util.Optional;
import java.util.TreeMap;


import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import com.iparvez.fileapi.demo.configs.UserConfig;
import com.iparvez.fileapi.demo.custom.Pair;
import com.iparvez.fileapi.demo.dao.User.UserDao;
import com.iparvez.fileapi.demo.dao.User.UserLoginDto;
import com.iparvez.fileapi.demo.dao.User.UserUpdateDto;
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
public class UserController {

    private final UserConfig userConfig;
    @Autowired private UserService userService; 
    
    @Autowired private jwtService jwtService; 
    
    @Autowired AuthenticationManager authenticationManager;

    UserController(UserConfig userConfig) {
        this.userConfig = userConfig;
    } 
    
    @GetMapping("/hello")
    public String testPath() {
        return new String("hello");
    }
    
    
    // find user by id
    @GetMapping("/api/user/id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable long userId) {
        try {
            UserDao userDetails = this.userService.getUserById(userId); 
            if(userDetails.getStatus()==UserEnum.NOT_FOUND){
                return new ResponseEntity<UserDao>(HttpStatus.NOT_FOUND); 
            }    
            return new ResponseEntity<UserDao>(userDetails, HttpStatus.OK); 
        } catch (Exception e) {
            System.err.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY); 
        }     
        
    }
    
    
    // find by name
    @GetMapping("/api/user/name/{uName}")
    public ResponseEntity<?> getUserByName(@PathVariable String uName) {
        try {
            UserDao userDetails = this.userService.getUserByUsername(uName); 
            if(userDetails.getStatus()==UserEnum.NOT_FOUND){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
            }
            return new ResponseEntity<UserDao>(userDetails, HttpStatus.OK); 

        } catch (Exception e) {
            System.err.println(e);; 
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY); 
        }
    }
    
    // register
    @PostMapping("/api/user/register")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        // System.out.println(user.toString());
        try{
            UserDao userDetails = this.userService.getUserByUsername(user.getUsername()) ; 
            if(userDetails.getStatus()==UserEnum.FOUND){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
            }
            userDetails = this.userService.createOrUpdateUser(user); 
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY); 
        }
        
    }
    
    // login
    
    @PostMapping("/api/user/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDetails) {
        UserDao userDetails = this.userService.getUserByUsername(loginDetails.getUserName());
        if(userDetails.getStatus()==UserEnum.NOT_FOUND){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDetails.getUserName(), 
                loginDetails.getPassword()
            )
        ); 
        
        if(!authentication.isAuthenticated()){
            return new ResponseEntity<>(new UserDao(UserEnum.NOT_FOUND), HttpStatus.NOT_ACCEPTABLE); 
        }
        HttpHeaders headers = new HttpHeaders(); 
        headers.add("X-JWT-Token-Response", this.jwtService.generateToken(
            loginDetails.getUserName()
        )); 
        System.out.println(headers.toString());
        return new ResponseEntity<>(userDetails,headers, HttpStatus.OK) ; 
        
        // return entity;
    }
    
    @GetMapping("/api/user/me")
    public ResponseEntity<?> getCurrentUser(
        @AuthenticationPrincipal User user
    ) {
        try{
            return new ResponseEntity<UserDao>(
                new UserDao(user, UserEnum.FOUND), 
                HttpStatus.OK    
            ); 
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY) ;
        }
    }
    
    
    // update
    @PutMapping("/api/user/id/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable long userId, 
        @RequestBody UserUpdateDto updateDetails) {
        try {
            UserDao userdetails = this.userService.getUserById(userId); 
            if(userdetails.getStatus()==UserEnum.NOT_FOUND){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
            }
            if(updateDetails.getUserName()!=null){
                userdetails.getUser().setUserName(updateDetails.getUserName());
            }
            if(updateDetails.getPassword()!=null){
                userdetails.getUser().setPassword(updateDetails.getPassword());
            }
            this.userService.createOrUpdateUser(userdetails.getUser()); 
            userdetails.setStatus(UserEnum.UPDATED);
            return new ResponseEntity<>(userdetails, HttpStatus.OK) ;
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e);
            return new ResponseEntity<>(new UserDao(UserEnum.SERVER_ERROR), HttpStatus.BAD_GATEWAY); 
        }
    }
    
    @DeleteMapping("/api/user/id/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId){
        try {
            UserDao userDetails = this.userService.getUserById(userId) ;
            if(userDetails.getStatus()==UserEnum.NOT_FOUND){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST) ;
            }
            try{
                this.userService.deleteUser(userId); 
                return new ResponseEntity<>(new UserDao(UserEnum.DELETED), HttpStatus.OK); 
            }   
            catch(Exception e){
                System.err.println(e);
                return new ResponseEntity<>(new UserDao(
                    UserEnum.SERVER_ERROR
                ), HttpStatus.BAD_GATEWAY); 
            } 
        } catch (Exception e) {
            System.err.println(e);
            return new ResponseEntity<>(new UserDao(
                UserEnum.SERVER_ERROR
            ), HttpStatus.BAD_GATEWAY); 
        }
    }
    
    
}
