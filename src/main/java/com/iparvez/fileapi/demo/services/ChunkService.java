package com.iparvez.fileapi.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iparvez.fileapi.demo.models.Chunk;
import com.iparvez.fileapi.demo.repo.ChunkRepo;

@Service
public class ChunkService {
    @Autowired
    public ChunkRepo chunkRepo; 

    public Optional<Chunk> findById(long id){
        Optional<Chunk> chunk = this.chunkRepo.findById(id); 
        return chunk; 
    }

    public Chunk createChunk(Chunk chunk){
        this.chunkRepo.save(chunk); 
        return chunk; 
    }

    public boolean existsByFileIdAndChunkIndex(long fileId, int chunkIndex){
        return this.chunkRepo.existsByFileIdAndChunkIndex(fileId, chunkIndex); 
    }

    public Optional<Chunk> findByFileIdAndChunkIndex(long fileId, int chunkIndex){
        Optional<Chunk> chunk = this.chunkRepo.findByFileIdAndChunkIndex(fileId, chunkIndex); 
        return chunk; 
    }
}
