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

import com.example.demo.entity.PrizeTotalEntity;

@Repository
public class PrizeTotalRepository {
    
    private final ClassPathResource classPathResource;
    private final Properties properties;

    public PrizeTotalRepository() throws IOException {
        classPathResource = new ClassPathResource("prizeTotalEntities.properties");
        properties = new Properties();
        properties.load(new InputStreamReader(classPathResource.getInputStream(), Charset.forName("utf-8")));
    }

    public void save(PrizeTotalEntity prizeTotalEntity) throws IOException {
        properties.setProperty(String.valueOf(prizeTotalEntity.getId()), prizeTotalEntity.getName());
        properties.store(new FileWriter(classPathResource.getFile(), Charset.forName("utf-8")), "PrizeTotalEntities");
    }

    public PrizeTotalEntity findById(Long id) {
        String name = properties.getProperty(String.valueOf(id));
        return new PrizeTotalEntity(id, name);
    }

    public List<PrizeTotalEntity> findAll() {
        return properties.entrySet().stream().map(entry -> new PrizeTotalEntity(Long.valueOf(String.valueOf(entry.getKey())), String.valueOf(entry.getValue()))).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        properties.remove(String.valueOf(id));
    }

}
