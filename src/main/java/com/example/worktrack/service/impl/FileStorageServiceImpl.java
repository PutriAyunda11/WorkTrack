package com.example.worktrack.service.impl;

import com.example.worktrack.constant.ConstantValue;
import com.example.worktrack.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Override
    public String store(MultipartFile file){

        if(file == null || file.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"File is empty");
        }

        String originalFileName = file.getOriginalFilename();
        String extention = getFileExtention(originalFileName);
        String fileName = UUID.randomUUID()+extention;

        try {
            Path uploadPath = Paths.get(ConstantValue.BASE_UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            return ConstantValue.BASE_UPLOAD_DIR + fileName;
        }catch (IOException e){
            throw new RuntimeException("Filed to store file", e);

        }
    }

    private String getFileExtention(String fileName){
        if(fileName == null || !fileName.contains(".")){
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
