package com.example.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    
    String save(MultipartFile multipartFile) throws Exception;
    Resource findByFilename(String filename) throws Exception;

}
