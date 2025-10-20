package com.iparvez.fileapi.demo.dao;

import com.iparvez.fileapi.demo.models.User;

public class UserDto {
    public User returnUser(String userName){
        return new User(userName); 
    }
}
