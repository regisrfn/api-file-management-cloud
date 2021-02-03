package com.rufino.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    private SimpleClientHttpRequestFactory requestFactory;

    public RestConfig() {
        requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        requestFactory.setChunkSize(4096);
        requestFactory.setConnectTimeout(0);
    }

    @Bean
    public RestTemplate setUpRest() {
        return new RestTemplate(requestFactory);
    }

}
