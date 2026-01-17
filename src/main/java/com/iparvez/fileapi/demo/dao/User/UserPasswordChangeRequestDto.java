package com.iparvez.fileapi.demo.dao.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordChangeRequestDto {
    @Getter @Setter private String userName, password; 
}
