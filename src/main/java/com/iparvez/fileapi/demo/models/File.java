package com.iparvez.fileapi.demo.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor @NoArgsConstructor
@Data
@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id; 
    
    @Column(name = "name", nullable = false, unique = true)
    @Getter @Setter private String fileName; 

    // @Column(unique = true) private String name; 
    @Column(name="ext",nullable = false, length = 4)
    @Getter @Setter private String extension;
    
    @Column(name="mime", nullable = false, length = 20)
    @Getter @Setter public String mimeType; 


    @ManyToOne
    @JoinColumn(name="creator", referencedColumnName = "id", nullable = false)
    @Getter @Setter private User creator;
    
    
    
}
