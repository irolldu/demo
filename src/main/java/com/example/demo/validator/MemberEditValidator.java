package com.example.demo.validator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.dto.MemberDto;
import com.example.demo.service.MemberService;

@Component
public class MemberEditValidator implements Validator {
    
    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Properties memberDtoValidatorProperties;
    private final Properties regexProperties;

    public MemberEditValidator() throws IOException {
        ClassPathResource memberDtoValidatorPropertiesClassPathResource = new ClassPathResource("memberDtoValidator.properties");
        memberDtoValidatorProperties = new Properties();
        memberDtoValidatorProperties.load(new InputStreamReader(memberDtoValidatorPropertiesClassPathResource.getInputStream(), Charset.forName("utf-8")));
        ClassPathResource regexPropertiesClassPathResource = new ClassPathResource("regex.properties");
        regexProperties = new Properties();
        regexProperties.load(new InputStreamReader(regexPropertiesClassPathResource.getInputStream(), Charset.forName("utf-8")));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == MemberDto.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberDto memberDto = (MemberDto) target;
        if (!memberDto.isValidated()) {
            return;
        }
        if (!passwordEncoder.matches(memberDto.getPassword(), memberService.findByEmail(memberDto.getEmail()).getPassword())) {
            errors.rejectValue("password", "password.notEqual", memberDtoValidatorProperties.getProperty("password.notEqual"));
        }
        if (memberDto.getName().isBlank()) {
            errors.rejectValue("name", "name.notBlank", memberDtoValidatorProperties.getProperty("name.notBlank"));
        }
        if (memberDto.getPhone().isBlank()) {
            errors.rejectValue("phone", "phone.notBlank", memberDtoValidatorProperties.getProperty("phone.notBlank"));
        } else if (!memberDto.getPhone().matches(regexProperties.getProperty("phone"))) {
            errors.rejectValue("phone", "phone.notMatch", memberDtoValidatorProperties.getProperty("phone.notMatch"));
        }
        if (memberDto.getCompany().isBlank()) {
            errors.rejectValue("company", "company.notBlank", memberDtoValidatorProperties.getProperty("company.notBlank"));
        }
    }

}
