package com.ecommerce.project.CategoryService.configuration;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    Logger logger = LoggerFactory.getLogger(AppConfig.class);

    //     logger.debug("inside config");
    @Bean//Since its a 3rd party class, we only instatiate then let spring handle it as bean
    public ModelMapper categoryModelMapper() {
        logger.debug("inside config");
        return new ModelMapper();
    }
}
