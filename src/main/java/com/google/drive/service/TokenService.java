package com.google.drive.service;

import com.google.api.client.util.Value;
import com.google.drive.dto.ActionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private final String CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private final String CLIENT_SECRET;

    private final RestTemplate restTemplate;

    public String fetchToken(String code, String scope) {
        final String uri = "https://accounts.google.com/o/oauth2/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String clientCredentials = Base64.getEncoder().encodeToString((CLIENT_ID+":"+CLIENT_SECRET).getBytes());
        headers.add("Authorization", "Basic "+clientCredentials);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("redirect_uri", "http://localhost:8080/oauth2/callback/google");
        requestBody.add("scope", scope);

        HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);

        ResponseEntity<ActionResponse> response = restTemplate.exchange(uri, HttpMethod.POST, formEntity,  ActionResponse.class);
        return response.getBody().getAccess_token();
    }
}
