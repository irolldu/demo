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

import com.example.demo.entity.PrizeBenefitEntity;

@Repository
public class PrizeBenefitRepository {
    
    private final ClassPathResource classPathResource;
    private final Properties properties;

    public PrizeBenefitRepository() throws IOException {
        classPathResource = new ClassPathResource("prizeBenefitEntities.properties");
        properties = new Properties();
        properties.load(new InputStreamReader(classPathResource.getInputStream(), Charset.forName("utf-8")));
    }

    public void save(PrizeBenefitEntity prizeBenefitEntity) throws IOException {
        properties.setProperty(String.valueOf(prizeBenefitEntity.getId()), prizeBenefitEntity.getName());
        properties.store(new FileWriter(classPathResource.getFile(), Charset.forName("utf-8")), "PrizeBenefitEntities");
    }

    public PrizeBenefitEntity findById(Long id) {
        String name = properties.getProperty(String.valueOf(id));
        return new PrizeBenefitEntity(id, name);
    }

    public List<PrizeBenefitEntity> findAll() {
        return properties.entrySet().stream().map(entry -> new PrizeBenefitEntity(Long.valueOf(String.valueOf(entry.getKey())), String.valueOf(entry.getValue()))).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        properties.remove(String.valueOf(id));
    }

}
