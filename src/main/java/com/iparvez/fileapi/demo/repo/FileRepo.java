package com.iparvez.fileapi.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iparvez.fileapi.demo.models.FileData;

@Repository
public interface FileRepo extends JpaRepository<FileData,Long> {

}
