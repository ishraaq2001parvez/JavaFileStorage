package com.iparvez.fileapi.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iparvez.fileapi.demo.dao.Chunk.GetSingleChunkDao;
import com.iparvez.fileapi.demo.dao.Chunk.SaveChunkRequestDao;
import com.iparvez.fileapi.demo.dao.Chunk.SaveChunkResponseDao;
import com.iparvez.fileapi.demo.enums.ChunkEnum;
import com.iparvez.fileapi.demo.models.Chunk;
import com.iparvez.fileapi.demo.models.File;
import com.iparvez.fileapi.demo.repo.ChunkRepo;
import com.iparvez.fileapi.demo.repo.FileRepo;

@Service
public class FileUploadDownloadService {
    @Autowired private FileRepo fileRepo; 
    @Autowired private ChunkRepo chunkRepo ;

    /* create chunk and save it */
    public SaveChunkResponseDao uploadSingleChunk(SaveChunkRequestDao saveChunkRequestDao){
        try {
            SaveChunkResponseDao saveChunkResponseDao = new SaveChunkResponseDao() ;
            /* search for file */
            Optional<File> file = this.fileRepo.findById(saveChunkRequestDao.getFileId()); 
            /* if file is empty, set status and return */
            if(file.isEmpty()){
                saveChunkResponseDao.setStatus(ChunkEnum.NOT_FOUND);
                return saveChunkResponseDao ;
            }
            Chunk createdChunk = new Chunk(); 
            createdChunk.setChunkIndex(saveChunkRequestDao.getChunkindex());
            createdChunk.setData(saveChunkRequestDao.getData());
            createdChunk.setFile(file.get());
            /* save, set status, return */
            this.chunkRepo.save(createdChunk) ;
            saveChunkResponseDao.setStatus(ChunkEnum.CREATED);
            return saveChunkResponseDao ;


        } catch (Exception e) {
            SaveChunkResponseDao saveChunkResponseDao =new SaveChunkResponseDao() ;
            saveChunkResponseDao.setStatus(ChunkEnum.SERVER_ERROR);
            return saveChunkResponseDao ;
        }
    }

    /* retrieve chunk based on file id and chunk index */
    public GetSingleChunkDao downloadSingleChunk(Long fileId, Short chunkIndex){
        try {
            GetSingleChunkDao getSingleChunkDao = new GetSingleChunkDao() ;
            /* try to get chunk */
            Optional<Chunk> chunk = this.chunkRepo.findByFileIdAndChunkIndex(fileId, chunkIndex) ;
            if(chunk.isEmpty()){
                getSingleChunkDao.setStatus(ChunkEnum.NOT_FOUND);
                return getSingleChunkDao ;
            }
            getSingleChunkDao.setChunk(chunk.get());
            getSingleChunkDao.setStatus(ChunkEnum.FOUND);
            return getSingleChunkDao ;
        } catch (Exception e) {
            GetSingleChunkDao getSingleChunkDao = new GetSingleChunkDao() ;
            getSingleChunkDao.setStatus(ChunkEnum.SERVER_ERROR);
            return getSingleChunkDao ;
        }
    }
}
