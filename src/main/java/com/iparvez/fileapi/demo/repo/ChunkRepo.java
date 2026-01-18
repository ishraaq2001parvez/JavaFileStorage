package com.iparvez.fileapi.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iparvez.fileapi.demo.models.Chunk;

@Repository
public interface ChunkRepo extends JpaRepository<Chunk, Long>{
    /* to check if chunk exists by file index and chunk index */
    @Query(nativeQuery = true, value = "select exists(select 1 from chunk where fid=:fileIndex and cid=:chunkIndex)")
    boolean existsByFileIdAndChunkIndex(@Param("fileId") long fileId, @Param("chunkIndex") int chunkIndex); 
    
    /* find by chunk id and file id */
    @Query(nativeQuery = true, value = "select 1 from chunk where fid=:fileIndex and cid=:chunkIndex")
    Optional<Chunk> findByFileIdAndChunkIndex(@Param("fileId") long fileId, @Param("chunkIndex") int chunkIndex); 

    /* find all chunks by file id */
    @Query(nativeQuery = true, 
        value = "select * from chunk where fid=:fileId"
    )
    List<Chunk> findChunksByFileId(
        @Param("fileId") Long fileId
    ); 

    /* find all chunks with file id, but with limit and offset */
    @Query(nativeQuery = true, 
        value = "select * from chunk where fid=:fileId order by cid limit :limit offset :offset"
    )
    List<Chunk> findChunksByFileIdAndRange(
        @Param("fileId") Long fileId, 
        @Param("limit") Long limit, 
        @Param("offset") Long offset
    ); 
}
