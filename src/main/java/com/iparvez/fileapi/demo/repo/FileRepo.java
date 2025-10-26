package com.iparvez.fileapi.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iparvez.fileapi.demo.models.File;
import com.iparvez.fileapi.demo.models.User;

@Repository
public interface FileRepo extends JpaRepository<File,Long> {
    @Query(nativeQuery = true, value = "select * from file where name=:name")
    Optional<File> findByName(@Param("name") String name)  ; 

    @Query(nativeQuery = true, value="select * from creator where user_name=:userName")
    Optional<User> findCreator(@Param("userName") String userName); 

    @Query(nativeQuery = true, value = "select exists(select 1 from file where name=:fileName)")
    boolean existsByName(@Param("fileName") String fileName); 
}
