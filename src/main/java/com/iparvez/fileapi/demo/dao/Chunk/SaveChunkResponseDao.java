package com.iparvez.fileapi.demo.dao.Chunk;

import com.iparvez.fileapi.demo.enums.ChunkEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
public class SaveChunkResponseDao {
    @Getter @Setter private ChunkEnum status; 
}
