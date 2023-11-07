package ru.fisenko.shareAll.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {
    private RestTemplate restTemplate = new RestTemplate();
    @Value("${id.url}")
    String url;

    public String sendGetRequest() {
        String data = null;
        try{
            data = restTemplate.getForObject(url, String.class);
        }catch (ResourceAccessException e)
        {
            System.out.println(e.getMessage());
        }
        return data;
    }

}

