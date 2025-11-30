package com.iparvez.fileapi.demo.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
public class Directory {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    @Column(name = "id")
    private Long dir_id; 

    @Getter @Setter private String name; 

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Directory parent;

    // access type of dir 
    // 3 values - {0 : private, 1: protected, 2: public}
    @Column(name = "access_type", nullable = false)
    @Getter @Setter private int accessType; 

    // define users who have access 
    @ManyToMany
    @JoinTable(
        name = "dir_access", 
        joinColumns = @JoinColumn(name="dir_id"),
        inverseJoinColumns = @JoinColumn(name ="id")
    )
    @Getter @Setter private Set<User> usersGranted; 


}
