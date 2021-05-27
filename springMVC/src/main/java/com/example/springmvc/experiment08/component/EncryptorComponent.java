package com.example.springmvc.experiment08.component;


import com.example.springmvc.example02.exception.MyException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EncryptorComponent {
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${my.salt}")
    private String salt;
    @Value("${my.secretkey}")
    private String secretKey;
    @Qualifier("getEncryptor01")
    @Autowired
    private TextEncryptor encryptor;

    @Bean
    public TextEncryptor getEncryptor01(){
        return Encryptors.text(secretKey,salt);
    }
    public String encrypt(Map<String,Object> payload){
        try {
            String json = objectMapper.writeValueAsString(payload);
            return encryptor.encrypt(json);
        } catch (JsonProcessingException e) {
            throw new MyException(500,"服务器错误");
        }
    }

    public Map<String,Object> decrypt(String auth){
        try {
            String json = encryptor.decrypt(auth);
            return objectMapper.readValue(json,Map.class);
        } catch (JsonProcessingException e) {
            throw new MyException(403,"无权限");
        }
    }
}
