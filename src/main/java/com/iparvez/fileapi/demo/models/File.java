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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor @NoArgsConstructor
@Data
@Entity
public class File {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    @Column(name = "id")
    private Long file_id; 
    
    // name of file
    @Column(name = "name", nullable = false, unique = true)
    @Getter @Setter private String fileName; 

    // extension 
    @Column(name="ext",nullable = false, length = 4)
    @Getter @Setter private String extension;
    
    // mimeType used for saving file 
    @Column(name="mime", nullable = false, length = 20)
    @Getter @Setter private String mimeType; 


    // map user creator
    @ManyToOne
    @JoinColumn(name="creator", referencedColumnName = "id", nullable = false)
    @Getter @Setter private User creator;

    // access type of file 
    // 3 values - {0 : private, 1: protected, 2: public}
    // only creator can access private dir (default when creation)
    // you need to raise message to access protected dir
    // public dir is accessible by all
    @Column(name = "access_type", nullable = false)
    @Getter @Setter private int accessType; 

    // define users who have access 
    @ManyToMany
    @JoinTable(
        name = "users_access", 
        joinColumns = @JoinColumn(name="file_id"),
        inverseJoinColumns = @JoinColumn(name ="id")
    )
    @Getter @Setter private Set<User> usersGranted; 

    @ManyToOne
    @JoinColumn(name = "dir", referencedColumnName = "id")
    @Getter @Setter private Directory dir;    
    
}
