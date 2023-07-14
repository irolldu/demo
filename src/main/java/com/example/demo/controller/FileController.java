package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileService;

@RestController
public class FileController {
    
    @Autowired
    private FileService fileService;

    @PostMapping(path = "/files")
    public void add(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()) {
            
        } else {
            fileService.save(multipartFile);
        }
    }

    @GetMapping(path = "/files/{filename}")
    public Resource view(@PathVariable(name = "filename") String filename) throws Exception {
        return fileService.findByFilename(filename);
    }

}
