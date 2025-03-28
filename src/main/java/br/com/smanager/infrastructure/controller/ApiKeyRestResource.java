package br.com.smanager.infrastructure.controller;

import br.com.smanager.domain.service.ApiKeyService;
import br.com.smanager.infrastructure.dto.ApiKeyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = RestConstants.PATH_API_KEYS)
@RequiredArgsConstructor
public class ApiKeyRestResource {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyDto> create(){

        var apiKey = apiKeyService.createApiKey();

        return ResponseEntity
            .created(URI.create(RestConstants.PATH_API_KEYS + "/" + apiKey.getId()))
            .body(ApiKeyDto.from(apiKey));
    }
}
