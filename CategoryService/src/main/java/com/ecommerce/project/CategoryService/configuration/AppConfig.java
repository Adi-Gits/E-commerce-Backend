package com.ecommerce.project.CategoryService.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean//Since its a 3rd party class, we only instatiate then let spring handle it as bean
    public ModelMapper categoryModelMapper(){
        return new ModelMapper();
    }
}
