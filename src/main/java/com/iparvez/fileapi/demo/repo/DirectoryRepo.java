package com.iparvez.fileapi.demo.repo;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iparvez.fileapi.demo.models.Directory;
import com.iparvez.fileapi.demo.models.User;

@Repository
public interface DirectoryRepo extends JpaRepository <Directory, Long>{

    /* query to count directory by parent and creator */
    /* 3 inner joins, to get parents, and then to get creators */
    /* this works, because we select by username AND parent name */
    /* so even if another user has a same directory, it will return false */
    @Query(value = "select count(d.id) from directory "+
                    " where d.parent_id= :parent_id and d.creator_id= :creator_id"+
                    " and d.name = :directoryName"
    , nativeQuery = true)
    Long count_by_directoryName_and_parentId_and_creatorId(
        @Param("directoryName") String directoryName, 
        @Param("parent_id") Long parentId, 
        @Param("creator_id") Long creatorId        
    ); 


    /* query to get all directory by parent and creator */
    /* 3 inner joins, to get parents, and then to get creators */
    /* this works, because we select by username AND parent name */
    /* so even if another user has a same directory, it will return false */
    @Query(value = "select d.* from directory "+
                    " where d.parent_id= :parent_id and d.creator_id= :creator_id"+
                    " and d.name = :directoryName"
    , nativeQuery = true)
    ArrayList<Directory> findAll_by_parentId_and_creatorId(
        @Param("parent_id") Long parent_id, 
        @Param("creator_id") Long creator_id
    ); 


    /* get creator via id */
    @Query(value = "select c.* from creator"+
                    " where creator.id = :creator_id", 
            nativeQuery = true
    )
    Optional<User> findByCreatorId(
        @Param("creator_id") Long creator_id
    ); 

    

    



}
