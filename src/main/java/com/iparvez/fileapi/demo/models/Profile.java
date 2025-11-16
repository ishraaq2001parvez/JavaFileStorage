package com.iparvez.fileapi.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Entity
@Data
@Table(name = "profile")
public class Profile {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Getter private long id; 

    @OneToOne
    @JoinColumn(name = "creator", nullable = false, referencedColumnName = "id")
    @Getter @Setter public User creator; 

    @Column(name = "email", length = 50)
    @Getter @Setter public String email;
    
    @Column(name = "pfp")
    @Lob public byte[] pfp; 


}
