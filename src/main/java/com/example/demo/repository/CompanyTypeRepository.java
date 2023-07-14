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

import com.example.demo.entity.CompanyTypeEntity;

@Repository
public class CompanyTypeRepository {
    
    private final ClassPathResource classPathResource;
    private final Properties properties;

    public CompanyTypeRepository() throws IOException {
        classPathResource = new ClassPathResource("companyTypeEntities.properties");
        properties = new Properties();
        properties.load(new InputStreamReader(classPathResource.getInputStream(), Charset.forName("utf-8")));
    }

    public void save(CompanyTypeEntity companyTypeEntity) throws IOException {
        properties.setProperty(String.valueOf(companyTypeEntity.getId()), companyTypeEntity.getName());
        properties.store(new FileWriter(classPathResource.getFile(), Charset.forName("utf-8")), "CompanyTypeEntities");
    }

    public CompanyTypeEntity findById(Long id) {
        String name = properties.getProperty(String.valueOf(id));
        return new CompanyTypeEntity(id, name);
    }

    public List<CompanyTypeEntity> findAll() {
        return properties.entrySet().stream().map(entry -> new CompanyTypeEntity(Long.valueOf(String.valueOf(entry.getKey())), String.valueOf(entry.getValue()))).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        properties.remove(String.valueOf(id));
    }

}
