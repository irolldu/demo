package com.example.demo.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
    
    private final Path multipartLocation;

    public FileServiceImpl() {
        multipartLocation = Paths.get("c:\\files\\");
    }

    @Override
    public String save(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()) {
            throw new Exception();
        }
        Path target = rename(multipartFile);
        if (!target.getParent().equals(multipartLocation.toAbsolutePath())) {
            throw new Exception();
        }
        Files.copy(multipartFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.getFileName().toString();
    }

    @Override
    public Resource findByFilename(String filename) throws Exception {
        Path path = multipartLocation.resolve(filename);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new Exception();
        }
    }

    private Path rename(MultipartFile multipartFile) throws Exception {
        String name = multipartFile.getOriginalFilename();
        String body = null;
        String ext = null;
        
        if (name == null) {
            throw new Exception();
        }
        int dot = name.lastIndexOf(".");
        if (dot != -1) {
            body = name.substring(0, dot);
            ext = name.substring(dot);
        } else {
            body = name;
            ext = "";
        }
        
        Path path = multipartLocation.resolve(Paths.get(name)).normalize().toAbsolutePath();
        int count = 0;
        while(Files.exists(path)) {
            count++;
            String newName = body + count + ext;
            path = multipartLocation.resolve(Paths.get(newName)).normalize().toAbsolutePath();
        }
        return path;
    }

}
