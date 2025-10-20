package com.iparvez.fileapi.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iparvez.fileapi.demo.models.Chunk;

@Repository
public interface ChunkRepo extends JpaRepository<Chunk, Long>{
    @Query(nativeQuery = true, value = "select exists(select 1 from chunk where fid=:fileIndex and cid=:chunkIndex)")
    boolean existsByFileIdAndChunkIndex(@Param("fileId") long fileId, @Param("chunkIndex") int chunkIndex); 
    
    @Query(nativeQuery = true, value = "select 1 from chunk where fid=:fileIndex and cid=:chunkIndex")
    Optional<Chunk> findByFileIdAndChunkIndex(@Param("fileId") long fileId, @Param("chunkIndex") int chunkIndex); 
}
