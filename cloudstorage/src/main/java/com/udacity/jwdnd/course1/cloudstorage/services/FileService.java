package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int createFile(File file) {
        return fileMapper.insertFile(new File(null, file.getFileName(), file.getContentType(), file.getSize(), file.getUserId(), file.getFileData()));
    }

    public List<File> getAllFiles(int userId) {
        return fileMapper.getFiles(userId);
    }

    public int updateFile(File file) {
        return fileMapper.updateFile(file);
    }

    public int deleteFile(int fileId) {
        return fileMapper.deleteFile(fileId);
    }

    public File findFileByName(String fileName) {
        return fileMapper.getFileByName(fileName);
    }

    public File findFileById(int fileId) {
        return fileMapper.getFileById(fileId);
    }

}
