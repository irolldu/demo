package com.example.demo.repository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CategoryEntity;

@Repository
public class CategoryRepository {
    
    private final ClassPathResource classPathResource;
    private final Properties properties;

    public CategoryRepository() throws IOException {
        classPathResource = new ClassPathResource("categoryEntities.properties");
        properties = new Properties();
        properties.load(new InputStreamReader(classPathResource.getInputStream(), Charset.forName("utf-8")));
    }

    public void save(CategoryEntity categoryEntity) throws IOException {
        properties.setProperty(String.valueOf(categoryEntity.getId()), categoryEntity.getName());
        properties.store(new FileWriter(classPathResource.getFile(), Charset.forName("utf-8")), "CategoryEntities");
    }

    public CategoryEntity findById(Long id) {
        String name = properties.getProperty(String.valueOf(id));
        return new CategoryEntity(id, name);
    }

    public List<CategoryEntity> findAll() {
        return properties.entrySet().stream().map(entry -> new CategoryEntity(Long.valueOf(String.valueOf(entry.getKey())), String.valueOf(entry.getValue()))).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        properties.remove(String.valueOf(id));
    }

}
