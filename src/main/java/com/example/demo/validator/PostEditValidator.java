package com.example.demo.validator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.dto.PostDto;

@Component
public class PostEditValidator implements Validator {

    private final Properties postDtoValidatorProperties;
    private final Properties regexProperties;

    public PostEditValidator() throws IOException {
        ClassPathResource postDtoValidatorPropertiesClassPathResource = new ClassPathResource("postDtoValidator.properties");
        postDtoValidatorProperties = new Properties();
        postDtoValidatorProperties.load(new InputStreamReader(postDtoValidatorPropertiesClassPathResource.getInputStream(), Charset.forName("utf-8")));
        ClassPathResource regexPropertiesClassPathResource = new ClassPathResource("regex.properties");
        regexProperties = new Properties();
        regexProperties.load(new InputStreamReader(regexPropertiesClassPathResource.getInputStream(), Charset.forName("utf-8")));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == PostDto.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        PostDto postDto = (PostDto) target;
        if (!postDto.isValidated()) {
            return;
        }
        if (postDto.getTitle().isBlank()) {
            errors.rejectValue("title", "title.notBlank", postDtoValidatorProperties.getProperty("title.notBlank"));
        }
        if (postDto.getWebsite().isBlank()) {
            errors.rejectValue("website", "website.notBlank", postDtoValidatorProperties.getProperty("website.notBlank"));
        } else if (!postDto.getWebsite().matches(regexProperties.getProperty("url"))) {
            errors.rejectValue("website", "website.notMatch", postDtoValidatorProperties.getProperty("website.notMatch"));
        }
        if (postDto.getCategoryIds().isEmpty()) {
            errors.rejectValue("categoryIds", "categoryIds.notEmpty", postDtoValidatorProperties.getProperty("categoryIds.notEmpty"));
        }
        if (postDto.getStartDate() == null || postDto.getEndDate() == null) {
            if (postDto.getStartDate() == null) {
                errors.rejectValue("startDate", "startDate.notNull", postDtoValidatorProperties.getProperty("startDate.notNull"));
            }
            if (postDto.getEndDate() == null) {
                errors.rejectValue("endDate", "endDate.notNull", postDtoValidatorProperties.getProperty("endDate.notNull"));
            }
        } else if (postDto.getStartDate().isAfter(postDto.getEndDate())) {
            errors.rejectValue("startDate", "startDate.notAfterEndDate", postDtoValidatorProperties.getProperty("startDate.notAfterEndDate"));
            errors.rejectValue("endDate", "endDate.notBeforeStartDate", postDtoValidatorProperties.getProperty("endDate.notBeforeStartDate"));
        }
        if (postDto.getCompany().isBlank()) {
            errors.rejectValue("company", "company.notBlank", postDtoValidatorProperties.getProperty("company.notBlank"));
        }
        if (postDto.getCompanyTypeId() == null) {
            errors.rejectValue("companyTypeId", "companyTypeId.notNull", postDtoValidatorProperties.getProperty("companyTypeId.notNull"));
        }
        if (postDto.getCompany2().isBlank()) {
            errors.rejectValue("company2", "company2.notBlank", postDtoValidatorProperties.getProperty("company2.notBlank"));
        }
        if (postDto.getCompany3().isBlank()) {
            errors.rejectValue("company3", "company3.notBlank", postDtoValidatorProperties.getProperty("company3.notBlank"));
        }
        if (postDto.getPrizeTop() == null) {
            errors.rejectValue("prizeTop", "prizeTop.notNull", postDtoValidatorProperties.getProperty("prizeTop.notNull"));
        }
        if (postDto.getPrizeTotalId() == null) {
            errors.rejectValue("prizeTotalId", "prizeTotalId.notNull", postDtoValidatorProperties.getProperty("prizeTotalId.notNull"));
        }
        if (postDto.getPrizeBenefitIds().isEmpty()) {
            errors.rejectValue("prizeBenefitIds", "prizeBenefitIds.notEmpty", postDtoValidatorProperties.getProperty("prizeBenefitIds.notEmpty"));
        }
        if (postDto.getDescription().isBlank()) {
            errors.rejectValue("description", "description.notBlank", postDtoValidatorProperties.getProperty("description.notBlank"));
        }
    }
    
}
