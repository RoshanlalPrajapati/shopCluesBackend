package com.example.demo1.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dbdww4fwp",
                "api_key", "799733973265264",
                "api_secret", "W4E-tEI6RHb9OpVojd0PxEPp2Eg"
        ));
    }

}
