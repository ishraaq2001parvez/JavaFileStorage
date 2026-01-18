package com.iparvez.fileapi.demo.dao.Chunk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
public class SaveChunkRequestDao {
    @Getter @Setter private Long fileId; 
    @Getter @Setter private byte[] data; 
    @Getter @Setter private Short chunkindex; 
}
