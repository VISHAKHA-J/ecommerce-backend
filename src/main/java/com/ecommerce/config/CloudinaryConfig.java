package com.ecommerce.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//Agenda of this file: normally images are not stored in database as it is
//they are stored as urls
//for that we need buckets for these images
//one bucket provider is Cloudinary
//uska sab configuration(connection) like cloud name , api key, api secret hum spring boot ko batate hai through this file
@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(
                ObjectUtils.asMap(         //ObjectUtils is a class ; asMap converts object to Map(object is returned as Map)
                        "cloud_name",cloudName,
                        "api_key",apiKey,
                        "api_secret",apiSecret,
                        "secure",true  // request goes as https and not http
                )
        );
    }


}
