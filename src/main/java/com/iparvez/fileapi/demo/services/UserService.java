package com.iparvez.fileapi.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.iparvez.fileapi.demo.custom.Pair;
import com.iparvez.fileapi.demo.dao.User.UserDao;
import com.iparvez.fileapi.demo.enums.UserEnum;
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

    public UserDao getUserByUsername(String username){ 
        /*
         * attempt to retrieve a user by username
         */
        try {
            // check if user exists, if exists, return with appropriate type and find it
            Optional<User> user = this.userRepo.findByName(username) ;
            if(user.isEmpty()){
                return new UserDao(UserEnum.NOT_FOUND); 
            }
            return new UserDao(user.get(), UserEnum.FOUND); 
        } catch (Exception e) {
            System.err.println(e);
            return new UserDao(UserEnum.SERVER_ERROR); 
        }

    }
    
    public UserDao getUserById(Long id){
        try {
            Optional<User> user = this.userRepo.findById(id); 
            if(user.isEmpty()){
                return new UserDao(UserEnum.NOT_FOUND); 
            }
            return new UserDao(user.get(), UserEnum.FOUND); 
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e);
            return new UserDao(UserEnum.SERVER_ERROR); 
        }
    }
    
    public UserDao createOrUpdateUser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        this.userRepo.save(user);
        return new UserDao(user, UserEnum.CREATED) ;
    }
    
    public UserDao deleteUser(long id){
        if(!this.userRepo.existsById(id)){
            return new UserDao(UserEnum.NOT_FOUND); 
        }
        this.userRepo.deleteById(id);
        return new UserDao(UserEnum.DELETED);
    }
}
