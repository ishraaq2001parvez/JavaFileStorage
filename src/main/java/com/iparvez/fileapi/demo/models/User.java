package com.iparvez.fileapi.demo.models;

import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
// import org.hibernate.annotations.DialectOverride.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Data
@Entity
@Table(name = "creator")
public class User implements UserDetails {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private long id; 
    
    @Column(unique = true, nullable = false)
    @Setter private String userName; 


    @Column(nullable = false)
    @ColumnDefault("'password'")
    @Setter private String password; 


    


    

    /*
     * all these methods are needed for spring security
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
