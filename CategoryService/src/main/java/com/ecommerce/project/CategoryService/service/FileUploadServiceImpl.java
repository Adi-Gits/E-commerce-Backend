package com.ecommerce.project.CategoryService.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //logic to create filename and filepath
        String originalFileName = file.getOriginalFilename();
        String randaomId = UUID.randomUUID().toString();
        String filename = randaomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filepath = path + File.separator + filename;

        //checking folder/filepath exists , if not create new
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdir();

        //uploading image--coping this inputstream at this path
        Files.copy(file.getInputStream(), Paths.get(filepath));
        return filename;

    }
}
