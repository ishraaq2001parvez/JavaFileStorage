package com.iparvez.fileapi.demo.dao.User;

import com.iparvez.fileapi.demo.enums.UserEnum;
import com.iparvez.fileapi.demo.models.User;

public class UserDao{
    private User user; 
    private UserEnum searchResult;
    public UserDao(UserEnum status){
        this.searchResult = status; 
    }
    public UserDao(User user, UserEnum status){
        this.searchResult = status; 
        this.user = user; 
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public UserEnum getStatus() {
        return searchResult;
    }
    public void setStatus(UserEnum status) {
        this.searchResult = status;
    } 
    
}
