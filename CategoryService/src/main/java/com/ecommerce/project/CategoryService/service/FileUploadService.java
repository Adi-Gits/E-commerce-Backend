package com.ecommerce.project.CategoryService.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String uploadImage(String path, MultipartFile file) throws IOException;
}
