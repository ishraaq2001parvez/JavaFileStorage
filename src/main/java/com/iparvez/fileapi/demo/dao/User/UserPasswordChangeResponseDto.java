package com.iparvez.fileapi.demo.dao.User;

import com.iparvez.fileapi.demo.enums.UserEnum;
import com.iparvez.fileapi.demo.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
public class UserPasswordChangeResponseDto {
    @Getter @Setter private User user; 
    @Getter @Setter private UserEnum status; 
}
