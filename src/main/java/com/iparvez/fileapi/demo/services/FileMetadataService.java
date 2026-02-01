package com.iparvez.fileapi.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iparvez.fileapi.demo.dao.File.GetFileMetadataDao;
import com.iparvez.fileapi.demo.dao.File.SaveFileMetaDataRequestDao;
import com.iparvez.fileapi.demo.dao.File.SaveFileMetaDataResponseDao;
import com.iparvez.fileapi.demo.enums.FileEnum;
import com.iparvez.fileapi.demo.models.Directory;
import com.iparvez.fileapi.demo.models.File;
import com.iparvez.fileapi.demo.models.User;
import com.iparvez.fileapi.demo.repo.DirectoryRepo;
import com.iparvez.fileapi.demo.repo.FileRepo;

@Service
public class FileMetadataService {
    @Autowired private FileRepo fileRepo; 
    @Autowired private DirectoryRepo directoryRepo ;


    /* create a file and save it */
    public SaveFileMetaDataResponseDao createFile(
        SaveFileMetaDataRequestDao saveFileMetaDataRequestDao, User creator
    ){
        try {
            /* create dao to return */
            SaveFileMetaDataResponseDao saveFileMetaDataResponseDao = new SaveFileMetaDataResponseDao();
            /* attempt to retrieve directory mentioned */
            Optional<Directory> currentDirectory = this.directoryRepo.findById(
                saveFileMetaDataRequestDao.getDirId()
            ); 
            /* if directory does not exist, return  */
            if(currentDirectory.isEmpty()){
                saveFileMetaDataResponseDao.setStatus(FileEnum.NOT_ALLOWED);
                return saveFileMetaDataResponseDao ;
            }

            /* if file with name exists already */
            /* todo : handle use case */

            
            /* create new file object */
            File file = new File() ;
            /* set parameters according to items recieved */
            file.setFileName(saveFileMetaDataRequestDao.getFileName());
            file.setCreator(creator);
            file.setDir(currentDirectory.get());
            file.setAccessType(saveFileMetaDataRequestDao.getAccessType());
            file.setChunkCount(saveFileMetaDataRequestDao.getChunkCount());
            /* save metadata items */
            file.setExtension(
                saveFileMetaDataRequestDao.getExtension()
            );
            file.setFileFooter(saveFileMetaDataRequestDao.getFileFooter());
            file.setFileHeader(saveFileMetaDataRequestDao.getFileHeader());
            file.setMimeType(saveFileMetaDataRequestDao.getMimeType());
            file.setFileSignature(saveFileMetaDataRequestDao.getFileSignature());
            

            /* save file, set status, return */
            this.fileRepo.save(file) ;
            saveFileMetaDataResponseDao.setFile(file);
            saveFileMetaDataResponseDao.setStatus(FileEnum.CREATED);
            return saveFileMetaDataResponseDao ;

        } catch (Exception e) {
            /* set status to server error, return */
            SaveFileMetaDataResponseDao saveFileMetaDataResponseDao = new SaveFileMetaDataResponseDao() ;
            saveFileMetaDataResponseDao.setStatus(FileEnum.SERVER_ERROR);
            return saveFileMetaDataResponseDao ;
        }
    }

    /* check if file exists and you can download it */
    public GetFileMetadataDao getFile(Long fileId){
        try {
            /* create a dao  */
            GetFileMetadataDao getFileMetadataDao = new GetFileMetadataDao(); 

            /* check if file exists */
            Optional<File> file = this.fileRepo.findById(fileId); 
            /* if file does not exist
                set status to not found, return
             */
            if(file.isEmpty()){
                getFileMetadataDao.setStatus(FileEnum.NOT_FOUND);
                return getFileMetadataDao ;
            }
            /* else, set file and status, and return */
            getFileMetadataDao.setFile(file.get());
            getFileMetadataDao.setStatus(FileEnum.FOUND);
            return getFileMetadataDao ;

        } catch (Exception e) {
            /* return server error status */
            GetFileMetadataDao getFileMetadataDao = new GetFileMetadataDao() ;
            getFileMetadataDao.setStatus(FileEnum.SERVER_ERROR);
            return getFileMetadataDao ;
        }
    }

    /* stream file directly to client */
    /*
        
     */
    
}
