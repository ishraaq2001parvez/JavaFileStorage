package com.iparvez.fileapi.demo.models;

import java.util.Set;
import java.util.TreeSet;

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

    /*name of directory */
    @Getter @Setter private String name; 


    /* parent of directory */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @Getter @Setter private Directory parent;

    /*creator of directory */
    @ManyToOne
    @JoinColumn(name = "creator_id")
    @Getter @Setter private User creator; 



    // access type of dir 
    // 3 values - {0 : private, 1: protected, 2: public}
    // only creator can access private dir (default when creation)
    // you need to raise message to access protected dir
    // public dir is accessible by all
    @Column(name = "access_type", nullable = false)
    @Getter @Setter private int accessType; 

    // define users who have  read access 
    @ManyToMany
    @JoinTable(
        name = "dir_read_access", 
        joinColumns = @JoinColumn(name="dir_id"),
        inverseJoinColumns = @JoinColumn(name ="id")
    )
    @Getter @Setter private TreeSet<User> usersGrantedRead; 
    

    // define users who have  read access 
    @ManyToMany
    @JoinTable(
        name = "dir_write_access", 
        joinColumns = @JoinColumn(name="dir_id"),
        inverseJoinColumns = @JoinColumn(name ="id")
    )
    @Getter @Setter private TreeSet<User> usersGrantedWrite; 


}
