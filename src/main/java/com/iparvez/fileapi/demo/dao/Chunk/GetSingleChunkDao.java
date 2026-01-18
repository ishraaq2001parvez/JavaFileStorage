package com.iparvez.fileapi.demo.dao.Chunk;

import com.iparvez.fileapi.demo.enums.ChunkEnum;
import com.iparvez.fileapi.demo.models.Chunk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
public class GetSingleChunkDao {
    @Getter @Setter private Chunk chunk; 
    @Getter @Setter private ChunkEnum status; 
}
