package ru.fisenko.shareAll.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {
    private RestTemplate restTemplate = new RestTemplate();
    @Value("${id.url}")
    String url;

    public String sendGetRequest() {
        return restTemplate.getForObject(url, String.class);
    }

}

