package com.mju.groupware.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mju.groupware.service.UserService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.util.UserInfoMethod;

@TestConfiguration
public class TestMvcSharedConfig {

    @Bean(name = "testInternalResourceViewResolver")
    ViewResolver testViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    UserInfoMethod userInfoMethod(UserService userService, StudentService studentService, ProfessorService professorService) {
        return new UserInfoMethod(userService, studentService, professorService);
    }
}


