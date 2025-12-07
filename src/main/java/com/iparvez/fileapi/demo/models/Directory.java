package com.iparvez.fileapi.demo.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "parent_id")
    @Getter @Setter @JsonIgnore private Directory parent;
    /* method to expose id while using jsonignore */
    public Long getParentId(){
        return this.parent == null ? null : this.parent.getDir_id(); 
    }

    /*creator of directory */
    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "creator_id")
    @Getter @Setter @JsonIgnore private User creator; 
    /* method to expose id while using jsonignore */
    public Long getCreatorId(){
        return this.getCreator().getId(); 
    }


    // access type of dir 
    // 3 values - {0 : private, 1: protected, 2: public}
    // only creator can access private dir (default when creation)
    // you need to raise message to access protected dir
    // public dir is accessible by all
    @Column(name = "access_type", nullable = false)
    @Getter @Setter private int accessType; 

    // define users who have  read access 
    @ManyToMany(
        fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "dir_read_access", 
        joinColumns = @JoinColumn(name="dir_id"),
        inverseJoinColumns = @JoinColumn(name ="id")
    )
    @Getter @Setter private Set<User> usersGrantedRead; 
    

    // define users who have  read access 
    @ManyToMany(
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "dir_write_access", 
        joinColumns = @JoinColumn(name="dir_id"),
        inverseJoinColumns = @JoinColumn(name ="id")
    )
    @Getter @Setter private Set<User> usersGrantedWrite; 


}
