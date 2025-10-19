package com.iparvez.fileapi.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iparvez.fileapi.demo.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    @Query(nativeQuery = true, value = "select * from creator where user_name=:name")
    Optional<User> findByName(@Param("name") String name); 
    
} 