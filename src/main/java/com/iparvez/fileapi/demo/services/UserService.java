package com.iparvez.fileapi.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.repo.UserRepo;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepo userRepo; 

    
    private BCryptPasswordEncoder bCryptPasswordEncoder =new BCryptPasswordEncoder(10); 
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        /*
         * just a filler code to implement spring security
         * checks if user is found
         */
        Optional<User> user =  this.userRepo.findByName(username); 
        if(user.isEmpty()){
            System.out.println("404 user not found");
            throw new UsernameNotFoundException("404 user not found"); 
        }
        return user.get();
        
        
    }

    public Optional<User> getUserByUsername(String username){
        Optional<User> user = this.userRepo.findByName(username); 
        return user; 
    }
    
    public Optional<User> getUserById(Long id){
        Optional<User> user=  this.userRepo.findById(id); 
        return user; 
    }
    
    public User createOrUpdateUser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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
